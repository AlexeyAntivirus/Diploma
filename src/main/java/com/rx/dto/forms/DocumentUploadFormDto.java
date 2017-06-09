package com.rx.dto.forms;

import com.rx.dao.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class DocumentUploadFormDto {

    @NotNull
    private MultipartFile multipartFile;

    @NotNull
    private DocumentType documentType;

    public DocumentUploadFormDto() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }


    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}
