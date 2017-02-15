package com.rx.controllers;

import com.rx.dto.FileUploadResultDto;
import com.rx.dto.FileUploadStatus;
import com.rx.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/upload")
public class FileUploadController {

    private FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService storageService) {
        this.fileStorageService = storageService;
    }

    @GetMapping
    public String getUploadForm() {
        return "upload";
    }

    @PostMapping
    public String handleUpload(@RequestParam(name = "file") MultipartFile file,
            HttpServletResponse response, Model model) {

        FileUploadResultDto result = this.fileStorageService.saveToStorage(file);
        FileUploadStatus fileUploadStatus = result.getFileUploadStatus();

        model.addAttribute("fileUploadStatus", fileUploadStatus);
        model.addAttribute("uploadedFileUUID", result.getUploadedFileUUID());

        switch (fileUploadStatus) {
            case EMPTY_OR_NULL_FILE:
                response.setStatus(400);
                break;
            case FILE_UPLOADED:
                response.setStatus(200);
                break;
            case INTERNAL_ERROR:
                response.setStatus(500);
                break;
        }

        return "upload-result";
    }


}
