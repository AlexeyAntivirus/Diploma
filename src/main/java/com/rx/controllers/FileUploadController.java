package com.rx.controllers;

import com.rx.dto.FileUploadResultDto;
import com.rx.dto.FileUploadStatus;
import com.rx.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public String handleUpload(@RequestParam("file") MultipartFile file,
                               HttpServletResponse response,
                               Model model) {

        FileUploadResultDto result = this.fileStorageService.saveToStorage(file);
        HttpStatus status;
        String shortStatus;
        String description;

        switch (result.getFileUploadStatus()) {
            case EMPTY_OR_NULL_FILE:
                status = HttpStatus.BAD_REQUEST;
                shortStatus = "Ошибка!";
                description = "Файл не указан";
                break;
            case FILE_UPLOADED:
                status = HttpStatus.OK;
                shortStatus = "Успех!";
                description = "Файл загружен";
                break;
            case INTERNAL_ERROR:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                shortStatus = "Ошибка!";
                description = "Возникли технические неполадки с сервером";
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                shortStatus = "Internal error!";
                description = "Upload status is not recognized!";
                break;
        }

        model.addAttribute("uploadedFileUUID", result.getUploadedFileUUID());
        model.addAttribute("shortStatus", shortStatus);
        model.addAttribute("description", description);
        response.setStatus(status.value());

        return "upload-result";
    }
}
