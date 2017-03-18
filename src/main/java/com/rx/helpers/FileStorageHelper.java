package com.rx.helpers;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageHelper {

    private static final Logger LOGGER = LogManager.getLogger(FileStorageHelper.class);

    private Path fileStoragePath;
    private boolean isRunningTests;

    FileStorageHelper() {
        isRunningTests = true;
    }

    @Autowired
    public FileStorageHelper(@Value("${app.storage.folder}") String fileStoragePath) {
        this.isRunningTests = false;

        doInitFileStorageFolder(fileStoragePath);
    }

    public String saveNewFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        Path desiredFilePath = this.fileStoragePath.resolve(filename).normalize();

        if (!desiredFilePath.startsWith(this.fileStoragePath)) {
            throw new FileUploadInvalidPathException("Filename contains path traversal payload. filename=" + filename);
        }

        Path uploadedFilePath;

        try {
            uploadedFilePath = Files.write(desiredFilePath, file.getBytes());
        } catch (IOException ioException) {
            throw new FileUploadIOException("File is not uploaded into storage, because io error occurs", ioException);
        }

        return uploadedFilePath.getFileName().toString();
    }

    public File getFileByName(String filename) {
        Path desiredFilePath = this.fileStoragePath.resolve(filename).normalize();

        if (!desiredFilePath.startsWith(this.fileStoragePath)) {
            throw new FileDownloadNotFoundException("filename contains path traversal payload. filename=" + filename);
        }

        File file = desiredFilePath.toFile();

        if (!isRunningTests && !file.isFile()) {
            throw new FileDownloadNotFoundException("invalid path. filename=" + filename);
        }

        return file;
    }

    private void doInitFileStorageFolder(String fileStoragePath) {
        Path path = Paths.get(fileStoragePath).normalize();

        try {
            this.fileStoragePath = Files.notExists(path) ? Files.createDirectory(path) : path;
        } catch (IOException ioException) {
            throw new BeanCreationException("File storage folder is not created!", ioException);
        }
    }

    void setFileStoragePath(Path fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }

    void setRunningTests(boolean isRunningTests) {
        this.isRunningTests = isRunningTests;
    }
}
