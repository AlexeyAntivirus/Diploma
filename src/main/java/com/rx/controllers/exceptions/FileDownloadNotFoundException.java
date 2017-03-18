package com.rx.controllers.exceptions;

public class FileDownloadNotFoundException extends RuntimeException {
    public FileDownloadNotFoundException() {
    }

    public FileDownloadNotFoundException(String message) {
        super(message);
    }

    public FileDownloadNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadNotFoundException(Throwable cause) {
        super(cause);
    }
}
