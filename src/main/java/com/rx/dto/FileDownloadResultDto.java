package com.rx.dto;

import org.springframework.core.io.FileSystemResource;

/**
 * Created by multi-view on 2/14/17.
 */
public class FileDownloadResultDto {

    private FileSystemResource fileSystemResource;

    private FileDownloadStatus fileDownloadStatus;

    public FileDownloadResultDto() {}

    public FileDownloadResultDto setFileSystemResource(FileSystemResource fileSystemResource) {
        this.fileSystemResource = fileSystemResource;
        return this;
    }

    public FileDownloadResultDto setDownoloadResultStatus(FileDownloadStatus fileDownloadStatus) {
        this.fileDownloadStatus = fileDownloadStatus;
        return this;
    }

    public FileDownloadStatus getFileDownloadStatus() {
        return fileDownloadStatus;
    }

    public FileSystemResource getFileSystemResource() {
        return fileSystemResource;
    }
}
