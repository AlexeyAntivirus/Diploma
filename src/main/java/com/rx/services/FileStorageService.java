package com.rx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by multi-view on 2/10/17.
 */


@Service
public class FileStorageService {

    private final Map<UUID, Path> fileStorageDB;

    private Path fileStorageFolder;

    @Autowired
    public FileStorageService() {
        this.fileStorageDB = new HashMap<>();
        try {
            this.fileStorageFolder = this.createStorageIfAbsent(
                    Paths.get(System.getProperty("user.home"), ".storage"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID upload(MultipartFile file) throws IOException {
        UUID fileUUID = UUID.randomUUID();
        Path uploadedFilePath = this.fileStorageFolder.resolve(
                file.getOriginalFilename());

        Files.write(uploadedFilePath, file.getBytes());

        this.fileStorageDB.put(fileUUID, uploadedFilePath);

        return fileUUID;
    }

    public Path download(UUID fileUUID) {
        return this.fileStorageDB.get(fileUUID);
    }

    private Path createStorageIfAbsent(Path fileStorageFolder) throws IOException {
        return Files.exists(fileStorageFolder) ?
                fileStorageFolder : Files.createDirectory(fileStorageFolder);
    }
}
