package com.rx.controllers;

import com.rx.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by multi-view on 2/10/17.
 */

@Controller
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService storageService) {
        this.fileStorageService = storageService;
    }

    @GetMapping(name = "/upload", value = "/upload")
    public String getUploadForm() {
        return "upload";
    }

    @PostMapping(name = "/upload", value = "/upload-result")
    public String handleUpload(
            @RequestParam(name = "file") MultipartFile file,
            Model model) {

        if (file.isEmpty()) {
            model.addAttribute("result", "<strong>Ошибка!</strong> Файл не указан.");
            model.addAttribute("statusLink", "<a href=\"/upload\">Попробовать заново</a>");
        } else {
            try {
                model.addAttribute("result", "<strong>Успех!</strong> Файл загружен на сервер.");
                model.addAttribute("statusLink",
                        String.format("<a href=\"/download?fileUUID=%s\">Ссылка на скачивание</a><br/>",
                                this.fileStorageService.upload(file)) +
                        "<a href=\"/upload\">Вернуться</a>");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("result", "<strong>Ошибка!</strong> Возникли проблемы с сервером.");
                model.addAttribute("statusLink", "<a href=\"/upload\">Вернуться</a>");
            }
        }

        return "upload-result";
    }
}
