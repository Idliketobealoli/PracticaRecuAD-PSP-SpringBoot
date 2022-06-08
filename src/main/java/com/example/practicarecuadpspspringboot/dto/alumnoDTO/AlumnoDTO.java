package com.example.practicarecuadpspspringboot.dto.alumnoDTO;

import com.example.practicarecuadpspspringboot.model.Curso;
import com.example.practicarecuadpspspringboot.model.Performance;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoDTO {
    private String id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(regexp = ".*@.*\\..*", message = "Must be a valid Email")
    private String email;

    private String createdAt;

    private String updatedAt;

    private String image;

    private Curso curso;

    private Set<Performance> modulos;
}
