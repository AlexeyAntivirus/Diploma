package com.rx.controllers;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.dto.FileDownloadResultDto;
import com.rx.services.FileStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Controller
@RequestMapping("/download")
public class FileDownloadController {

    private static final Logger LOGGER = LogManager.getLogger(FileDownloadController.class);

    private FileStorageService fileStorageService;

    @Autowired
    public FileDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public Resource handleDownload(@RequestParam("fileId") Long fileId, HttpServletResponse response) throws IOException {

        FileDownloadResultDto result = this.fileStorageService.getFileFromStorageById(fileId);
        FileSystemResource resource = result.getFileResource();
        String filename = UriUtils.encode(resource.getFilename(), StandardCharsets.UTF_8.name());

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename);
        response.setContentLengthLong(resource.contentLength());

        return resource;
    }

    @ExceptionHandler(value = FileDownloadNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void fileDownloadNotFoundExceptionHandler(FileDownloadNotFoundException e) {
        LOGGER.warn(e.getMessage(), e);
    }
}
