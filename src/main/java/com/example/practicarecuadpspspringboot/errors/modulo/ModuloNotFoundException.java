package com.example.practicarecuadpspspringboot.errors.modulo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModuloNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public ModuloNotFoundException(String id) {
        super("Modulo with id: "+id+" does not exist.");
    }
}
