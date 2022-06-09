package com.example.practicarecuadpspspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity(name = "performance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Performance {
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private Alumno alumno;

    @ManyToOne
    private Modulo modulo;

    @NotNull(message = "Calificacion cannot be null")
    @Min(value = 0, message = "Must be at least 0")
    @Max(value = 10, message = "Must be at most 10")
    private double calificacion;
}
