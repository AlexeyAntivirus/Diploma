package com.rx.dto;

import org.springframework.core.io.FileSystemResource;


public class FileDownloadResultDto {

    private final FileSystemResource fileSystemResource;

    private final FileDownloadStatus fileDownloadStatus;

    protected FileDownloadResultDto(FileDownloadResultDtoBuilder builder) {
        this.fileSystemResource = builder.fileSystemResource;
        this.fileDownloadStatus = builder.fileDownloadStatus;
    }

    public FileDownloadStatus getFileDownloadStatus() {
        return fileDownloadStatus;
    }

    public FileSystemResource getFileSystemResource() {
        return fileSystemResource;
    }

    public static FileDownloadResultDtoBuilder getBuilder() {
        return new FileDownloadResultDtoBuilder();
    }

    public static class FileDownloadResultDtoBuilder {

        private FileSystemResource fileSystemResource;

        private FileDownloadStatus fileDownloadStatus;

        public FileDownloadResultDtoBuilder setFileSystemResource(FileSystemResource fileSystemResource) {
            this.fileSystemResource = fileSystemResource;
            return this;
        }

        public FileDownloadResultDtoBuilder setFileDownloadStatus(FileDownloadStatus fileDownloadStatus) {
            this.fileDownloadStatus = fileDownloadStatus;
            return this;
        }

        public FileDownloadResultDto build() {
            return new FileDownloadResultDto(this);
        }
    }
}
