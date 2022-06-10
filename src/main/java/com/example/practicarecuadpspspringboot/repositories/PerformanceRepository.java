package com.example.practicarecuadpspspringboot.repositories;

import com.example.practicarecuadpspspringboot.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Deprecated
@Repository
public interface PerformanceRepository extends JpaRepository<Performance, UUID> {
    @Deprecated
    Optional<List<Performance>> findByAlumno_Email(String alumnoEmail);
    @Deprecated
    Optional<List<Performance>> findByModulo_Acronym(String moduloAcronym);
}
