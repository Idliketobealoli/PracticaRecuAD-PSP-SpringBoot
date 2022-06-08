package com.example.practicarecuadpspspringboot.dto.cursoDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListCursoDTO {
    private final String searchedAt = LocalDateTime.now().toString();
    private List<CursoDTO> data;
}
