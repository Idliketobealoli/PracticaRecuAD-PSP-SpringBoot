package com.example.practicarecuadpspspringboot.errors.curso;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CursosNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public CursosNotFoundException() {
        super("List of Curso is either empty or nonexistent.");
    }
}
