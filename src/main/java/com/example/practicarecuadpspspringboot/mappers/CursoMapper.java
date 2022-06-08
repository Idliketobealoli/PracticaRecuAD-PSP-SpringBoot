package com.example.practicarecuadpspspringboot.mappers;

import com.example.practicarecuadpspspringboot.dto.cursoDTO.CreateCursoDTO;
import com.example.practicarecuadpspspringboot.dto.cursoDTO.CursoDTO;
import com.example.practicarecuadpspspringboot.dto.cursoDTO.ListCursoDTO;
import com.example.practicarecuadpspspringboot.model.Curso;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CursoMapper {
    private final ModelMapper modelMapper;

    public CursoDTO toDTO(Curso curso) {
        return modelMapper.map(curso, CursoDTO.class);
    }

    public Curso fromDTO(CursoDTO cursoDTO) {
        return modelMapper.map(cursoDTO, Curso.class);
    }

    public Curso fromDTO(CreateCursoDTO cursoDTO) {
        return modelMapper.map(cursoDTO, Curso.class);
    }

    public List<CursoDTO> toDTO(List<Curso> cursos) {
        return cursos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ListCursoDTO toListDTO(List<Curso> cursos) {
        ListCursoDTO listDTO = new ListCursoDTO();
        listDTO.setData(cursos.stream().map(this::toDTO).collect(Collectors.toList()));
        return listDTO;
    }
}
