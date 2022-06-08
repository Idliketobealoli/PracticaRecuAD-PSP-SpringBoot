package com.example.practicarecuadpspspringboot.services;

import com.example.practicarecuadpspspringboot.dto.cursoDTO.CreateCursoDTO;
import com.example.practicarecuadpspspringboot.model.Curso;
import com.example.practicarecuadpspspringboot.repositories.CursoRepository;
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
public class CursoService {
    private final CursoRepository repository;

    public Optional<List<Curso>> findCursoByName(String name) {
        return repository.findByNameLikeIgnoreCase(name);
    }

    public Optional<Page<Curso>> findCursoByName(String name, Pageable page) {
        return repository.findByNameLikeIgnoreCase(name, page);
    }

    public Optional<Curso> findCursoById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Curso> findCursoByAcronym(String acronym) {
        return repository.findByAcronym(acronym);
    }

    public Curso createCurso(CreateCursoDTO newCurso) {
        Curso curso = Curso.builder()
                .number(newCurso.getNumber())
                .name(newCurso.getName())
                .acronym(newCurso.getAcronym())
                .modulos(newCurso.getModulos())
                .build();

        try {
            return repository.save(curso);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso already exists.");
        }
    }
}
