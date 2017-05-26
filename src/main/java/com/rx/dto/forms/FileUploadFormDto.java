package com.rx.dto.forms;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class FileUploadFormDto {

    @NotNull
    private MultipartFile multipartFile;

    public FileUploadFormDto() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
