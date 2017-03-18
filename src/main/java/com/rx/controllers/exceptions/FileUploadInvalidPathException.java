package com.rx.controllers.exceptions;

public class FileUploadInvalidPathException extends RuntimeException {
    public FileUploadInvalidPathException() {
    }

    public FileUploadInvalidPathException(String message) {
        super(message);
    }

    public FileUploadInvalidPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadInvalidPathException(Throwable cause) {
        super(cause);
    }
}
