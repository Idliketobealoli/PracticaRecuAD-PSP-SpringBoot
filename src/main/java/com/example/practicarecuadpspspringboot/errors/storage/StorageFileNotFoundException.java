package com.example.practicarecuadpspspringboot.errors.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageFileNotFoundException extends StorageException {
    private static final long serialVersionUID = 12345678987645L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
