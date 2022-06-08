package com.example.practicarecuadpspspringboot.controllers;

import com.example.practicarecuadpspspringboot.config.APIConfig;
import com.example.practicarecuadpspspringboot.repositories.AlumnoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConfig.API_PATH + "/alumnos")
public class AlumnoController {
    //private final AlumnoRepository repository;
    //private final AlumnoService service;
}
