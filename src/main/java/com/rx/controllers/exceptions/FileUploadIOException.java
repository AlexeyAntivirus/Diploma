package com.rx.controllers.exceptions;

public class FileUploadIOException extends RuntimeException {
    public FileUploadIOException() {
    }

    public FileUploadIOException(String message) {
        super(message);
    }

    public FileUploadIOException(Throwable cause) {
        super(cause);
    }

    public FileUploadIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
