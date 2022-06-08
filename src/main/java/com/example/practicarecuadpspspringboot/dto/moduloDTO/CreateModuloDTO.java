package com.example.practicarecuadpspspringboot.dto.moduloDTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuloDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Acronym cannot be blank")
    private String acronym;
}
