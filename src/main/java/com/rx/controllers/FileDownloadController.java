package com.rx.controllers;

import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileDownloadStatus;
import com.rx.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/download")
public class FileDownloadController {

    private FileStorageService fileStorageService;

    @Autowired
    public FileDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public @ResponseBody Resource handleDownload(
            @RequestParam("fileUUID") UUID fileUUID,
            HttpServletResponse response) throws IOException {

        FileDownloadResultDto result = this.fileStorageService.getFromStorage(fileUUID);
        FileDownloadStatus fileDownloadStatus = result.getFileDownloadStatus();

        FileSystemResource resource = result.getFileSystemResource();

        switch (fileDownloadStatus) {
            case FILE_FOUND:
                response.setStatus(200);
                response.setContentLengthLong(resource.contentLength());
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition",
                        String.format("attachment; filename=\"%s\"", resource.getFilename()));
                break;
            case FILE_NOT_FOUND:
                response.setStatus(404);
        }

        return resource;
    }
}
