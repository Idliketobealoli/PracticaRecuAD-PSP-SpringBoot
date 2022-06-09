package com.example.practicarecuadpspspringboot.errors.alumno;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlumnosNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public AlumnosNotFoundException() {
        super("List of Alumno is either empty or nonexistent.");
    }
}
