package com.rx.dto;

import java.util.UUID;

/**
 * Created by multi-view on 2/14/17.
 */
public class FileUploadResultDto {

    private UUID uploadedFileUUID;

    private FileUploadStatus fileUploadStatus;

    public FileUploadResultDto() {}

    public FileUploadResultDto setUploadedFileUUID(UUID uploadedFileUUID) {
        this.uploadedFileUUID = uploadedFileUUID;
        return this;
    }

    public FileUploadResultDto setFileUploadStatus(FileUploadStatus fileUploadStatus) {
        this.fileUploadStatus = fileUploadStatus;
        return this;
    }

    public FileUploadStatus getFileUploadStatus() {
        return fileUploadStatus;
    }

    public UUID getUploadedFileUUID() {
        return uploadedFileUUID;
    }
}
