package com.rx.services;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileUploadResultDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;
import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;

@Service
public class FileStorageService {

    private static final Logger LOGGER = LogManager.getLogger(FileStorageService.class);

    private Map<UUID, String> database;
    private Path fileStoragePath;

    @Autowired
    public FileStorageService(@Value("${app.storage.folder}") String fileStoragePath) {
        this.database = new ConcurrentHashMap<>();

        doInitFileStorageFolder(fileStoragePath);
    }

    public FileUploadResultDto saveToStorage(MultipartFile file) {
        FileUploadResultDtoBuilder resultBuilder = new FileUploadResultDtoBuilder();
        String filename = file.getOriginalFilename();
        Path desiredFilePath = this.fileStoragePath.resolve(filename).normalize();

        if (!desiredFilePath.startsWith(this.fileStoragePath)) {
            throw new FileUploadInvalidPathException("Received filename that contains path traversal payload. filename=" + filename);
        }

        try {
            Path uploadedFilePath = Files.write(desiredFilePath, file.getBytes());

            resultBuilder.withFileUUID(this.addToDatabase(uploadedFilePath.getFileName().toString()));
        } catch (IOException ioException) {
            throw new FileUploadIOException("File is not uploaded into storage, because io error occurs", ioException);
        }

        return resultBuilder.build();
    }

    public FileDownloadResultDto getFromStorage(UUID fileUUID) {
        FileDownloadResultDtoBuilder resultBuilder = new FileDownloadResultDtoBuilder();

        if (fileUUID == null) {
            return resultBuilder.build();
        }

        String filename = this.database.get(fileUUID);

        if (filename != null) {
            Path desiredFilePath = this.fileStoragePath.resolve(filename).normalize();

            if (!desiredFilePath.startsWith(this.fileStoragePath)) {
                throw new FileDownloadNotFoundException("filename stored in the DB contains path traversal payload. uuid=" + fileUUID);
            }

            resultBuilder.withFileResource(new FileSystemResource(desiredFilePath.toFile()));
        }

        return resultBuilder.build();
    }

    private UUID addToDatabase(String filename) {
        UUID uploadedFileUUID;
        String path;

        do {
            uploadedFileUUID = UUID.randomUUID();
            path = this.database.putIfAbsent(uploadedFileUUID, filename);
        } while (path != null);

        return uploadedFileUUID;
    }

    private void doInitFileStorageFolder(String fileStoragePath) {
        Path path = Paths.get(fileStoragePath).normalize();

        try {
            this.fileStoragePath = Files.notExists(path) ? Files.createDirectory(path) : path;
        } catch (IOException ioException) {
            throw new BeanCreationException("File storage folder is not created!", ioException);
        }
    }
}
