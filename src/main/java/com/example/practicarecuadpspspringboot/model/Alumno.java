package com.example.practicarecuadpspspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "alumno")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Alumno {
    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(regexp = ".*@.*\\..*", message = "Must be a valid Email")
    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotBlank(message = "Image cannot be blank")
    private String image;

    @ManyToOne
    private Curso curso;

    @OneToMany
    private Set<Performance> modulos;
}
