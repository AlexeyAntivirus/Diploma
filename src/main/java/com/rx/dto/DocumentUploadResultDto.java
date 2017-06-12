package com.rx.dto;

public class DocumentUploadResultDto {

    private final Long documentId;

    protected DocumentUploadResultDto(DocumentUploadResultDtoBuilder builder) {
        this.documentId = builder.fileId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public static DocumentUploadResultDtoBuilder builder() {
        return new DocumentUploadResultDtoBuilder();
    }

    public static class DocumentUploadResultDtoBuilder {

        private Long fileId;

        public DocumentUploadResultDtoBuilder withFileId(Long uploadedFileId) {
            this.fileId = uploadedFileId;
            return this;
        }

        public DocumentUploadResultDto build() {
            return new DocumentUploadResultDto(this);
        }
    }
}
