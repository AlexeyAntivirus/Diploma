package com.rx.controllers;

import com.rx.data.ServiceResult;
import com.rx.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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

    @PostMapping(name = "/upload", value = "/upload")
    public String handleUpload(@RequestParam(name = "file") MultipartFile file,
            HttpServletResponse response, Model model) {

        ServiceResult<UUID> result =
                this.fileStorageService.saveToStorage(file);

        model.addAttribute("status", result.getStatus().value());
        model.addAttribute("value", result.getValue());

        response.setStatus(result.getStatus().value());

        return "upload-result";
    }

    @GetMapping(name = "/upload-result", value = "/upload-result")
    public String getUploadResult() {
        return "upload-result";
    }
}
