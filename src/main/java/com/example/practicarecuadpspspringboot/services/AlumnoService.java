package com.example.practicarecuadpspspringboot.services;

import com.example.practicarecuadpspspringboot.dto.alumnoDTO.CreateAlumnoDTO;
import com.example.practicarecuadpspspringboot.model.Alumno;
import com.example.practicarecuadpspspringboot.repositories.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlumnoService {
    private final AlumnoRepository repository;

    public Optional<List<Alumno>> findAlumnoByName(String name) {
        return repository.findByNameLikeIgnoreCase(name);
    }

    public Optional<Page<Alumno>> findAlumnoByName(String name, Pageable page) {
        return repository.findByNameLikeIgnoreCase(name, page);
    }

    public Optional<Alumno> findAlumnoById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Alumno> findAlumnoByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Alumno createAlumno(CreateAlumnoDTO newAlumno) {
        Alumno alumno = Alumno.builder()
                .name(newAlumno.getName())
                .email(newAlumno.getEmail())
                .image(newAlumno.getImage())
                .curso(newAlumno.getCurso())
                .modulos(newAlumno.getModulos())
                .build();

        try {
            return repository.save(alumno);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alumno already exists.");
        }
    }
}
