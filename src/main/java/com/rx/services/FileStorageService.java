package com.rx.services;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileUploadResultDto;
import com.rx.helpers.FileStorageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;
import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;

@Service
public class FileStorageService {

    private static final Logger LOGGER = LogManager.getLogger(FileStorageService.class);

    private Map<UUID, String> database;
    private FileStorageHelper fileStorageHelper;

    @Autowired
    public FileStorageService(FileStorageHelper fileStorageHelper) {
        this.database = new ConcurrentHashMap<>();
        this.fileStorageHelper = fileStorageHelper;
    }

    public FileUploadResultDto saveFileInStorage(MultipartFile file) {
        String uploadedFilePath = fileStorageHelper.saveNewFile(file);
        UUID fileUuid = this.addToDatabase(uploadedFilePath);

        return new FileUploadResultDtoBuilder().withFileUUID(fileUuid).build();
    }

    public FileDownloadResultDto getFileFromStorageById(UUID fileUUID) {
        if (fileUUID == null) {
            throw new FileDownloadNotFoundException("fileUUID is missing!");
        }

        String filename = this.database.get(fileUUID);

        if (filename == null) {
            throw new FileDownloadNotFoundException("No file was found by fileUUID. fileUUID=" + fileUUID);
        }

        File file = fileStorageHelper.getFileByName(filename);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        return new FileDownloadResultDtoBuilder().withFileResource(fileSystemResource).build();
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
}
