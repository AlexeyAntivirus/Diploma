package com.rx.dto;

import java.util.UUID;

public class FileUploadResultDto {

    private final UUID fileUUID;

    protected FileUploadResultDto(FileUploadResultDtoBuilder builder) {
        this.fileUUID = builder.fileUUID;
    }

    public UUID getFileUUID() {
        return fileUUID;
    }

    public static class FileUploadResultDtoBuilder {

        private UUID fileUUID;

        public FileUploadResultDtoBuilder withFileUUID(UUID uploadedFileUUID) {
            this.fileUUID = uploadedFileUUID;
            return this;
        }

        public FileUploadResultDto build() {
            return new FileUploadResultDto(this);
        }
    }
}
