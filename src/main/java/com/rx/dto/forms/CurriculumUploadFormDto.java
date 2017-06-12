package com.rx.dto.forms;


import com.rx.dao.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class CurriculumUploadFormDto {

    private Long disciplineId;

    private DocumentType documentType;

    @NotNull
    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}
