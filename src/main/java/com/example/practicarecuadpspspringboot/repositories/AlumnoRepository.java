package com.example.practicarecuadpspspringboot.repositories;

import com.example.practicarecuadpspspringboot.model.Alumno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, UUID> {
    List<Alumno> findByNameLikeIgnoreCase(String name);
    Page<Alumno> findByNameLikeIgnoreCase(String name, Pageable pageable);

    Optional<Alumno> findByEmail(String email);
}
