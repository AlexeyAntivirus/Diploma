package com.rx.controllers;

import com.rx.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Created by multi-view on 2/10/17.
 */

@Controller
public class FileDownloadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(name = "/download", value = "/download")
    public ResponseEntity<Resource> handleDownload(
            @RequestParam("fileUUID") UUID fileUUID) {

        Path uploadedFilePath = fileStorageService.download(fileUUID);
        try {
            ByteArrayResource buffer = new ByteArrayResource(
                    Files.readAllBytes(uploadedFilePath));
            return ResponseEntity.ok()
                    .contentLength(buffer.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition",
                            String.format("attachment; filename=\"%s\"", uploadedFilePath.getFileName()))
                    .body(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}
