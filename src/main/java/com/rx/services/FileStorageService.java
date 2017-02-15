package com.rx.services;

import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileDownloadStatus;
import com.rx.dto.FileUploadResultDto;
import com.rx.dto.FileUploadStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class FileStorageService {

    private Logger logger = LogManager.getLogger(FileStorageService.class);

    private Map<UUID, Path> database;

    private Path storageFolder;

    @Autowired
    public FileStorageService(Environment environment) {
        this.database = new HashMap<>();
        try {
            this.storageFolder = this.createStorageIfAbsent(
                    Paths.get(environment.getProperty("app.storage.folder")));
        } catch (IOException e) {
            throw new BeanCreationException("File storage folder is not created!");
        }

    }

    public FileUploadResultDto saveToStorage(MultipartFile file) {
        FileUploadResultDto dto = new FileUploadResultDto();

        if (file.isEmpty() || (file == null)) {
            return dto.setUploadedFileUUID(null)
                    .setFileUploadStatus(FileUploadStatus.EMPTY_OR_NULL_FILE);
        }

        try {
            Path uploadedFilePath = this.storageFolder.resolve(file.getOriginalFilename());
            Files.write(uploadedFilePath, file.getBytes());
            UUID uploadedFileUUID = UUID.randomUUID();
            while (this.database.containsKey(uploadedFileUUID)) {
                uploadedFileUUID = UUID.randomUUID();
            }
            this.database.put(uploadedFileUUID, uploadedFilePath);

            return dto.setUploadedFileUUID(uploadedFileUUID)
                    .setFileUploadStatus(FileUploadStatus.FILE_UPLOADED);
        } catch (IOException e) {
            logger.warn("File is not uploaded into storage, because io error occurs", e);
            return dto.setUploadedFileUUID(null)
                    .setFileUploadStatus(FileUploadStatus.INTERNAL_ERROR);
        }
    }

    public FileDownloadResultDto getFromStorage(UUID fileUUID) {
        FileDownloadResultDto dto = new FileDownloadResultDto();
        Path filePath = this.database.get(fileUUID);

        if (filePath != null) {
            return dto.setFileSystemResource(new FileSystemResource(filePath.toFile()))
                    .setDownoloadResultStatus(FileDownloadStatus.FILE_FOUND);
        } else {
            return dto.setFileSystemResource(null)
                    .setDownoloadResultStatus(FileDownloadStatus.FILE_NOT_FOUND);
        }
    }

    private Path createStorageIfAbsent(Path storageFolder) throws IOException {
        return Files.exists(storageFolder) ? storageFolder : Files.createDirectory(storageFolder);
    }
}
