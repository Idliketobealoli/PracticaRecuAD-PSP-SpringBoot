package com.example.practicarecuadpspspringboot.dto.cursoDTO;

import com.example.practicarecuadpspspringboot.model.Modulo;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCursoDTO {
    @NotBlank(message = "Number cannot be blank")
    @Min(value = 1, message = "Minimum must be 1")
    private int number;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Acronym cannot be blank")
    private String acronym;

    private Set<Modulo> modulos;
}
