package com.rx.services;

import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileDownloadStatus;
import com.rx.dto.FileUploadResultDto;
import com.rx.dto.FileUploadStatus;

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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;
import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;


@Service
public class FileStorageService {

    private static final Logger LOGGER = LogManager.getLogger(FileStorageService.class);

    private Map<UUID, Path> database;

    private Path storageFolder;

    @Autowired
    public FileStorageService(@Value("${app.storage.folder}") String storageFolder) {
        this.database = new HashMap<>();

        try {
            this.storageFolder = Files.notExists(Paths.get(storageFolder)) ?
                    Files.createDirectory(this.storageFolder) : Paths.get(storageFolder);
        } catch (IOException ioException) {
            throw new BeanCreationException("File storage folder is not created!", ioException);
        }
    }

    public FileUploadResultDto saveToStorage(MultipartFile file) {
        UUID uploadedFileUUID;
        FileUploadStatus uploadStatus;

        if (file == null || file.isEmpty()) {
            uploadedFileUUID = null;
            uploadStatus = FileUploadStatus.EMPTY_OR_NULL_FILE;
        } else {
            try {
                Path uploadedFilePath = this.storageFolder.resolve(file.getOriginalFilename());
                Files.write(uploadedFilePath, file.getBytes());

                uploadedFileUUID = this.addToDatabase(uploadedFilePath);
                uploadStatus = FileUploadStatus.FILE_UPLOADED;
            } catch (IOException ioException) {
                LOGGER.warn("File is not uploaded into storage, because io error occurs", ioException);
                uploadedFileUUID = null;
                uploadStatus = FileUploadStatus.INTERNAL_ERROR;
            }
        }

        return new FileUploadResultDtoBuilder().setUploadedFileUUID(uploadedFileUUID)
                .setFileUploadStatus(uploadStatus).build();
    }

    public FileDownloadResultDto getFromStorage(UUID fileUUID) {

        Path filePath = this.database.get(fileUUID);
        FileSystemResource resource;
        FileDownloadStatus downloadStatus;

        if (filePath != null) {
            resource = new FileSystemResource(filePath.toFile());
            downloadStatus = FileDownloadStatus.FILE_FOUND;
        } else {
            resource = null;
            downloadStatus = FileDownloadStatus.FILE_NOT_FOUND;
        }

        return new FileDownloadResultDtoBuilder().setFileSystemResource(resource)
                .setFileDownloadStatus(downloadStatus).build();
    }

    private Path createStorageIfAbsent(Path storageFolder) throws IOException {
        return Files.exists(storageFolder) ? storageFolder : Files.createDirectory(storageFolder);
    }

    private UUID addToDatabase(Path uploadedFilePath) {
        UUID uploadedFileUUID = UUID.randomUUID();
        while (this.database.containsKey(uploadedFileUUID)) {
            uploadedFileUUID = UUID.randomUUID();
        }
        this.database.put(uploadedFileUUID, uploadedFilePath);
        return uploadedFileUUID;
    }
}
