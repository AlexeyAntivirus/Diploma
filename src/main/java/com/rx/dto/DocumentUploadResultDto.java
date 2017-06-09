package com.rx.dto;

public class DocumentUploadResultDto {

    private final Long documentId;

    protected DocumentUploadResultDto(FileUploadResultDtoBuilder builder) {
        this.documentId = builder.fileId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public static class FileUploadResultDtoBuilder {

        private Long fileId;

        public FileUploadResultDtoBuilder withFileId(Long uploadedFileId) {
            this.fileId = uploadedFileId;
            return this;
        }

        public DocumentUploadResultDto build() {
            return new DocumentUploadResultDto(this);
        }
    }
}
