package com.example.practicarecuadpspspringboot.errors.alumno;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlumnoNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 12345678987645L;

    public AlumnoNotFoundException(String id) {
        super("Alumno with id: "+id+" does not exist.");
    }
}
