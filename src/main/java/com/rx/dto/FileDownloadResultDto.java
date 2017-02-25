package com.rx.dto;

import org.springframework.core.io.FileSystemResource;

public class FileDownloadResultDto {

    private final FileSystemResource fileResource;

    protected FileDownloadResultDto(FileDownloadResultDtoBuilder builder) {
        this.fileResource = builder.fileResource;
    }

    public FileSystemResource getFileResource() {
        return fileResource;
    }

    public static class FileDownloadResultDtoBuilder {

        private FileSystemResource fileResource;

        public FileDownloadResultDtoBuilder withFileResource(FileSystemResource fileResource) {
            this.fileResource = fileResource;
            return this;
        }

        public FileDownloadResultDto build() {
            return new FileDownloadResultDto(this);
        }
    }
}
