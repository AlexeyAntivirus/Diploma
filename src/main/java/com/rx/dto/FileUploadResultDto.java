package com.rx.dto;

public class FileUploadResultDto {

    private final Long fileId;

    protected FileUploadResultDto(FileUploadResultDtoBuilder builder) {
        this.fileId = builder.fileId;
    }

    public Long getFileId() {
        return fileId;
    }

    public static class FileUploadResultDtoBuilder {

        private Long fileId;

        public FileUploadResultDtoBuilder withFileUUID(Long uploadedFileId) {
            this.fileId = uploadedFileId;
            return this;
        }

        public FileUploadResultDto build() {
            return new FileUploadResultDto(this);
        }
    }
}
