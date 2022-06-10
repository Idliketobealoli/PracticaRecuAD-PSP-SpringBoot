package com.example.practicarecuadpspspringboot.services;

import com.example.practicarecuadpspspringboot.dto.moduloDTO.CreateModuloDTO;
import com.example.practicarecuadpspspringboot.model.Modulo;
import com.example.practicarecuadpspspringboot.repositories.ModuloRepository;
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
public class ModuloService {
    private final ModuloRepository repository;

    public Optional<List<Modulo>> findModuloByName(String name) {
        return repository.findByNameLikeIgnoreCase(name);
    }

    @Deprecated
    public Optional<Page<Modulo>> findModuloByName(String name, Pageable page) {
        return repository.findByNameLikeIgnoreCase(name, page);
    }

    public Optional<Modulo> findModuloById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Modulo> findModuloByAcronym(String acronym) {
        return repository.findByAcronym(acronym);
    }

    @Deprecated
    public Modulo createModulo(CreateModuloDTO newModulo) {
        Modulo modulo = Modulo.builder()
                .name(newModulo.getName())
                .acronym(newModulo.getAcronym())
                .build();

        try {
            return repository.save(modulo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modulo already exists.");
        }
    }
}
