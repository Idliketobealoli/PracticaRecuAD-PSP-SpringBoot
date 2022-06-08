package com.example.practicarecuadpspspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Modulo {
    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Acronym cannot be blank")
    @Column(unique = true)
    private String acronym;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    //private Curso curso;

    //@OneToMany
    //private Set<Performance> alumnos;
}
