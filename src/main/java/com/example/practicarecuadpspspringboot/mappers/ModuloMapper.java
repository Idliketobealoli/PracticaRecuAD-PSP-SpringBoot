package com.example.practicarecuadpspspringboot.mappers;

import com.example.practicarecuadpspspringboot.dto.moduloDTO.CreateModuloDTO;
import com.example.practicarecuadpspspringboot.dto.moduloDTO.ListModuloDTO;
import com.example.practicarecuadpspspringboot.dto.moduloDTO.ModuloDTO;
import com.example.practicarecuadpspspringboot.model.Modulo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModuloMapper {
    private final ModelMapper modelMapper;

    public ModuloDTO toDTO(Modulo modulo) {
        return modelMapper.map(modulo, ModuloDTO.class);
    }

    @Deprecated
    public Modulo fromDTO (ModuloDTO moduloDTO) {
        return modelMapper.map(moduloDTO, Modulo.class);
    }

    public Modulo fromDTO (CreateModuloDTO moduloDTO) {
        return modelMapper.map(moduloDTO, Modulo.class);
    }

    public List<ModuloDTO> toDTO(List<Modulo> modulos) {
        return modulos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Deprecated
    public ListModuloDTO toListDTO(List<Modulo> modulos) {
        ListModuloDTO listDTO = new ListModuloDTO();
        listDTO.setData(modulos.stream().map(this::toDTO).collect(Collectors.toList()));
        return listDTO;
    }
}
