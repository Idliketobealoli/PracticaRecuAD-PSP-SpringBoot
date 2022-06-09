package com.example.practicarecuadpspspringboot.errors.modulo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ModuloBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public ModuloBadRequestException(String field, String error) {
        super("There's a mistake in field: "+field+" --- Message: "+error);
    }
}
