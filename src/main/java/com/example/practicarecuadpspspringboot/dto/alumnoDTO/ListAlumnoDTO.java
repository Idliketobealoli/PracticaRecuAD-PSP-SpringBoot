package com.example.practicarecuadpspspringboot.dto.alumnoDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListAlumnoDTO {
    private final String searchedAt = LocalDateTime.now().toString();
    private List<AlumnoDTO> data;
}
