package com.example.practicarecuadpspspringboot.dto.moduloDTO;

import com.example.practicarecuadpspspringboot.model.Performance;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuloDTO {
    private String id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Acronym cannot be blank")
    private String acronym;

    private String createdAt;

    //private Curso curso;

    //private Set<Performance> alumnos;
}
