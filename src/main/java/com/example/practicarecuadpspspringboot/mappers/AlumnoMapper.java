package com.example.practicarecuadpspspringboot.mappers;

import com.example.practicarecuadpspspringboot.dto.alumnoDTO.AlumnoDTO;
import com.example.practicarecuadpspspringboot.dto.alumnoDTO.CreateAlumnoDTO;
import com.example.practicarecuadpspspringboot.dto.alumnoDTO.ListAlumnoDTO;
import com.example.practicarecuadpspspringboot.model.Alumno;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlumnoMapper {
    private final ModelMapper modelMapper;

    public AlumnoDTO toDTO(Alumno alumno) {
        return modelMapper.map(alumno, AlumnoDTO.class);
    }

    public Alumno fromDTO(AlumnoDTO alumnoDTO) {
        return modelMapper.map(alumnoDTO, Alumno.class);
    }

    public Alumno fromDTO(CreateAlumnoDTO alumnoDTO) {
        return modelMapper.map(alumnoDTO, Alumno.class);
    }

    public List<AlumnoDTO> toDTO(List<Alumno> alumnos) {
        return alumnos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /*
    public List<Alumno> fromDTO(List<AlumnoDTO> alumnosDTO) {
        return alumnosDTO.stream().map(this::fromDTO).collect(Collectors.toList());
    }
     */

    public ListAlumnoDTO toListDTO(List<Alumno> alumnos) {
        ListAlumnoDTO listDTO = new ListAlumnoDTO();
        listDTO.setData(alumnos.stream().map(this::toDTO).collect(Collectors.toList()));
        return listDTO;
    }
}
