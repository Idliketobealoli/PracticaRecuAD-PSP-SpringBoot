package com.example.practicarecuadpspspringboot.repositories;

import com.example.practicarecuadpspspringboot.model.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CursoRepository extends JpaRepository<Curso, UUID> {
    Optional<List<Curso>> findByNameLikeIgnoreCase(String name);
    Optional<Page<Curso>> findByNameLikeIgnoreCase(String name, Pageable pageable);
    Optional<Page<Curso>> findByNumber(int number, Pageable pageable);
    Optional<Page<Curso>> findByNameLikeIgnoreCaseAndNumber(String name, int number, Pageable pageable);

    Optional<Curso> findByAcronym(String acronym);
}
