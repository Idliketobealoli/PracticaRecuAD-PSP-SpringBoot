package com.example.practicarecuadpspspringboot.controllers;

import com.example.practicarecuadpspspringboot.config.APIConfig;
import com.example.practicarecuadpspspringboot.dto.cursoDTO.CursoDTO;
import com.example.practicarecuadpspspringboot.dto.moduloDTO.CreateModuloDTO;
import com.example.practicarecuadpspspringboot.dto.moduloDTO.ListModuloPageDTO;
import com.example.practicarecuadpspspringboot.dto.moduloDTO.ModuloDTO;
import com.example.practicarecuadpspspringboot.errors.GenericBadRequestException;
import com.example.practicarecuadpspspringboot.errors.curso.CursoBadRequestException;
import com.example.practicarecuadpspspringboot.errors.curso.CursoNotFoundException;
import com.example.practicarecuadpspspringboot.errors.modulo.ModuloBadRequestException;
import com.example.practicarecuadpspspringboot.errors.modulo.ModuloNotFoundException;
import com.example.practicarecuadpspspringboot.errors.modulo.ModulosNotFoundException;
import com.example.practicarecuadpspspringboot.mappers.ModuloMapper;
import com.example.practicarecuadpspspringboot.model.Modulo;
import com.example.practicarecuadpspspringboot.repositories.ModuloRepository;
import com.example.practicarecuadpspspringboot.services.ModuloService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(APIConfig.API_PATH + "/modulos")
public class ModuloController {
    private final ModuloRepository repository;
    private final ModuloService service;
    private final ModuloMapper mapper;

    @Autowired
    public ModuloController(ModuloRepository repository, ModuloService service, ModuloMapper mapper) {
        this.repository = repository;
        this.service = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "test", notes = "Test")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = String.class)})
    @GetMapping("/test")
    public String test(){
        return "ModuloRepository.test() funcionando.";
    }

    @ApiOperation(value = "findAll", notes = "Finds all Modulo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ModuloDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not Found", response = ModulosNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @GetMapping("/")
    public ResponseEntity<List<ModuloDTO>> findAll(@RequestParam(required = false, name = "limit") Optional<String> limit,
                                                  @RequestParam(required = false, name = "name")Optional<String> name) {
        List<Modulo> modulos = null;
        try {
            if (name.isPresent()) {
                modulos = service.findModuloByName(name.get()).orElse(null);
            } else {
                modulos = repository.findAll();
            }

            if (limit.isPresent() && !modulos.isEmpty() && modulos.size() > Integer.parseInt(limit.get())) {
                return ResponseEntity.ok(mapper.toDTO(modulos.subList(0, Integer.parseInt(limit.get()))));
            } else {
                if (!modulos.isEmpty()) {
                    return ResponseEntity.ok(mapper.toDTO(modulos));
                } else {
                    throw new ModulosNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GenericBadRequestException("Data selection", "Incorrect parameters.");
        }
    }

    @ApiOperation(value = "Find Modulo by id", notes = "Finds a Modulo by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ModuloDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ModuloNotFoundException.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTO> findById(@PathVariable String id) {
        Modulo modulo = service.findModuloById(UUID.fromString(id)).orElse(null);
        if (modulo == null) {
            throw new ModuloNotFoundException(id);
        } else {
            return ResponseEntity.ok(mapper.toDTO(modulo));
        }
    }

    @ApiOperation(value = "Find Modulo by acronym", notes = "Finds a Modulo by acronym")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = CursoNotFoundException.class)
    })
    @GetMapping("/acronym={acronym}")
    public ResponseEntity<ModuloDTO> findByAcronym(@PathVariable String acronym) {
        Modulo curso = service.findModuloByAcronym(acronym).orElse(null);
        if (curso == null) {
            throw new ModuloNotFoundException(acronym);
        } else {
            return ResponseEntity.ok(mapper.toDTO(curso));
        }
    }

    @ApiOperation(value = "Save a Modulo", notes = "Saves a Modulo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saved", response = ModuloDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @PostMapping("/")
    public ResponseEntity<ModuloDTO> save(@RequestBody CreateModuloDTO moduloDTO) {
        Modulo modulo = mapper.fromDTO(moduloDTO);
        checkData(modulo);
        try {
            Modulo insertedModulo = repository.save(modulo);
            return ResponseEntity.ok(mapper.toDTO(insertedModulo));
        } catch (Exception e) {
            throw new GenericBadRequestException("Insert", "Unable to save Modulo. Incorrect fields. --- "+e.getMessage());
        }
    }

    @ApiOperation(value = "Update a Modulo", notes = "Updates a Modulo with the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ModuloDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ModuloNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ModuloDTO> update(@PathVariable String id, @RequestBody Modulo modulo) {
        Modulo updatedModulo = repository.findById(UUID.fromString(id)).orElse(null);
        if (updatedModulo == null) {
            throw new ModuloNotFoundException(id);
        }
        checkData(modulo);

        updatedModulo.setName(modulo.getName());
        updatedModulo.setAcronym(modulo.getAcronym());
        try {
            updatedModulo = repository.save(updatedModulo);
            return ResponseEntity.ok(mapper.toDTO(updatedModulo));
        } catch (Exception e) {
            throw new GenericBadRequestException("Update", "Unable to update Modulo. Incorrect fields. --- "+e.getMessage());
        }
    }

    @ApiOperation(value = "Delete a Modulo", notes = "Deletes a Modulo by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ModuloDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ModuloNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ModuloDTO> delete(@PathVariable String id) {
        Modulo modulo = repository.findById(UUID.fromString(id)).orElse(null);
        if (modulo == null) {
            throw new ModuloNotFoundException(id);
        }
        try {
            repository.delete(modulo);
            return ResponseEntity.ok(mapper.toDTO(modulo));
        } catch (Exception e) {
            throw new GenericBadRequestException("Delete", "Unable to delete Modulo. --- "+e.getMessage());
        }
    }

    private void checkData(Modulo modulo) {
        if (modulo.getName() == null || modulo.getName().isEmpty()) {
            throw new ModuloBadRequestException("Name", "Name is mandatory.");
        }
        if (modulo.getAcronym() == null || modulo.getAcronym().isEmpty()) {
            throw new CursoBadRequestException("Acronym", "Acronym is mandatory.");
        }
    }

    @ApiOperation(value = "Finds all Modulo in a paged list.", notes = "Returns a paged, ordered and filtered list.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ListModuloPageDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @GetMapping("/all")
    public ResponseEntity<?> pagedList(
            @RequestParam(required = false, name = "name") Optional<String> name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Pageable paging = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Modulo> pagedResult;

        try {
            if (name.isPresent()) {
                pagedResult = repository.findByNameLikeIgnoreCase(name.get(), paging).orElse(null);
            } else {
                pagedResult = repository.findAll(paging);
            }

            ListModuloPageDTO lModuloPageDTO = ListModuloPageDTO.builder()
                    .data(mapper.toDTO(pagedResult.getContent()))
                    .totalPages(pagedResult.getTotalPages())
                    .totalElements(pagedResult.getTotalElements())
                    .currentPage(pagedResult.getNumber())
                    .sort(pagedResult.getSort().toString())
                    .build();
            return ResponseEntity.ok(lModuloPageDTO);
        } catch (Exception e) {
            throw new GenericBadRequestException("Data selection", "Incorrect parameters.");
        }
    }
}
