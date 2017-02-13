package com.rx.services;

import com.rx.data.ServiceResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by multi-view on 2/10/17.
 */


@Service
public class FileStorageService {

    private Logger logger = LogManager.getRootLogger();

    private Map<UUID, Path> database;

    private Path storageFolder;

    private FileSystemProvider provider = FileSystems.getDefault().provider();


    @Autowired
    public FileStorageService(Environment environment) {
        this.database = new HashMap<>();
        this.storageFolder = Paths.get(environment.getProperty("app.storage.folder"));
        File storageFolderFile = this.storageFolder.toFile();

        if (!storageFolderFile.exists()) {
            try {
                this.provider.createDirectory(this.storageFolder);
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public ServiceResult<UUID> saveToStorage(MultipartFile file) {

        if (file.isEmpty()) {
            return new ServiceResult<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            Path uploadedFilePath = this.storageFolder.resolve(file.getOriginalFilename());
            OutputStream outputStream = this.provider.newOutputStream(uploadedFilePath);
            outputStream.write(file.getBytes());
            UUID uploadedFileUUID = UUID.randomUUID();
            this.database.put(uploadedFileUUID, uploadedFilePath);

            return new ServiceResult<>(uploadedFileUUID, HttpStatus.OK);
        } catch (IOException e) {
            logger.error(e);
            return new ServiceResult<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ServiceResult<FileSystemResource> getFromStorage(UUID fileUUID) {
        Path filePath = this.database.get(fileUUID);

        if (filePath != null) {
            return new ServiceResult<>(
                    new FileSystemResource(filePath.toFile()), HttpStatus.OK);
        } else {
            return new ServiceResult<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
