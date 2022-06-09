package com.example.practicarecuadpspspringboot.errors.curso;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CursoNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public CursoNotFoundException(String id) {
        super("Curso with id: "+id+" does not exist.");
    }
}
