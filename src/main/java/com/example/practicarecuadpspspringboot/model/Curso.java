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

@Entity(name = "curso")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Curso {
    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "Number cannot be blank")
    private int number;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "Acronym cannot be blank")
    private String acronym;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany
    private Set<Modulo> modulos;

    // private List<Alumno> alumnos;
}
