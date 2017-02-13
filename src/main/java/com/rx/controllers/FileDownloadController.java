package com.rx.controllers;

import com.rx.data.ServiceResult;
import com.rx.services.FileStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * Created by multi-view on 2/10/17.
 */

@Controller
public class FileDownloadController {

    private Logger logger = LogManager.getRootLogger();

    private final FileStorageService fileStorageService;

    @Autowired
    public FileDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(name = "/download", value = "/download")
    public void handleDownload(
            @RequestParam("fileUUID") UUID fileUUID,
            HttpServletResponse response) {

        ServiceResult<FileSystemResource> result =
                this.fileStorageService.getFromStorage(fileUUID);

        if (result.getStatus() == HttpStatus.OK) {
            try {
                FileSystemResource resource = result.getValue();
                response.setContentLengthLong(resource.contentLength());
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition",
                        String.format("attachment; filename=\"%s\"", resource.getFilename()));

                IOUtils.copy(resource.getInputStream(), response.getOutputStream());
            } catch (IOException e) {
                logger.error(e);
            }
        }

        response.setStatus(result.getStatus().value());
    }
}
