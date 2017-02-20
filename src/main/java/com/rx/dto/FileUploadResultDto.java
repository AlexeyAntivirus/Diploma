package com.rx.dto;

import java.util.UUID;


public class FileUploadResultDto {

    private final UUID uploadedFileUUID;

    private final FileUploadStatus fileUploadStatus;

    protected FileUploadResultDto(FileUploadResultDtoBuilder builder) {
        this.uploadedFileUUID = builder.uploadedFileUUID;
        this.fileUploadStatus = builder.fileUploadStatus;
    }

    public FileUploadStatus getFileUploadStatus() {
        return fileUploadStatus;
    }

    public UUID getUploadedFileUUID() {
        return uploadedFileUUID;
    }

    public static FileUploadResultDtoBuilder getBuilder() {
        return new FileUploadResultDtoBuilder();
    }

    public static class FileUploadResultDtoBuilder {

        private UUID uploadedFileUUID;

        private FileUploadStatus fileUploadStatus;

        public FileUploadResultDtoBuilder setUploadedFileUUID(UUID uploadedFileUUID) {
            this.uploadedFileUUID = uploadedFileUUID;
            return this;
        }

        public FileUploadResultDtoBuilder setFileUploadStatus(FileUploadStatus fileUploadStatus) {
            this.fileUploadStatus = fileUploadStatus;
            return this;
        }

        public FileUploadResultDto build() {
            return new FileUploadResultDto(this);
        }
    }
}
