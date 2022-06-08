package com.example.practicarecuadpspspringboot.dto.cursoDTO;

import com.example.practicarecuadpspspringboot.model.Modulo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {
    private String id;

    @NotBlank(message = "Number cannot be blank")
    private int number;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Acronym cannot be blank")
    private String acronym;

    private String createdAt;

    private Set<Modulo> modulos;

    //private List<Alumno> alumnos;
}
