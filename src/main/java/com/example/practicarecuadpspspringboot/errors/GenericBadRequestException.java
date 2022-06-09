package com.example.practicarecuadpspspringboot.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GenericBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public GenericBadRequestException(String operation, String errorMessage) {
        super("Error at: "+operation+" --- Message: "+errorMessage);
    }
}
