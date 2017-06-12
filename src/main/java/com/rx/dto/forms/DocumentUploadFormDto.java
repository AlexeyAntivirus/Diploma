package com.rx.dto.forms;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class DocumentUploadFormDto {

    @NotNull
    private MultipartFile multipartFile;

    public DocumentUploadFormDto() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
