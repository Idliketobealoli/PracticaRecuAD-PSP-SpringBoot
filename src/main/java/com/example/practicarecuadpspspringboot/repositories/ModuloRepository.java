package com.example.practicarecuadpspspringboot.repositories;

import com.example.practicarecuadpspspringboot.model.Modulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, UUID> {
    Optional<List<Modulo>> findByNameLikeIgnoreCase(String name);
    Optional<Page<Modulo>> findByNameLikeIgnoreCase(String name, Pageable pageable);

    Optional<Modulo> findByAcronym(String acronym);
}
