package com.example.practicarecuadpspspringboot.dto.moduloDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ListModuloPageDTO {
    private final String searchedAt = LocalDateTime.now().toString();
    private List<ModuloDTO> data;

    @Min(value = 0, message = "Minimum page number must be 0")
    private int currentPage;

    @Min(value = 1, message = "Minimum amount of elements per page must be 1")
    private long totalElements;

    private int totalPages;
}
