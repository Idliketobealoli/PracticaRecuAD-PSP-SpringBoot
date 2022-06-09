package com.example.practicarecuadpspspringboot.errors.curso;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CursoBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public CursoBadRequestException(String field, String error) {
        super("There's a mistake in field: "+field+" --- Message: "+error);
    }
}
