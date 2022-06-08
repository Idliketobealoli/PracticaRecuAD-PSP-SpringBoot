package com.example.practicarecuadpspspringboot.dto.moduloDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListModuloDTO {
    private final String searchedAt = LocalDateTime.now().toString();
    private List<ModuloDTO> data;
}
