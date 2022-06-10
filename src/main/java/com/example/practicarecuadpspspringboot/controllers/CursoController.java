package com.example.practicarecuadpspspringboot.controllers;

import com.example.practicarecuadpspspringboot.config.APIConfig;
import com.example.practicarecuadpspspringboot.dto.cursoDTO.CreateCursoDTO;
import com.example.practicarecuadpspspringboot.dto.cursoDTO.CursoDTO;
import com.example.practicarecuadpspspringboot.dto.cursoDTO.ListCursoPageDTO;
import com.example.practicarecuadpspspringboot.errors.GenericBadRequestException;
import com.example.practicarecuadpspspringboot.errors.curso.CursoBadRequestException;
import com.example.practicarecuadpspspringboot.errors.curso.CursoNotFoundException;
import com.example.practicarecuadpspspringboot.errors.curso.CursosNotFoundException;
import com.example.practicarecuadpspspringboot.mappers.CursoMapper;
import com.example.practicarecuadpspspringboot.model.Curso;
import com.example.practicarecuadpspspringboot.model.Modulo;
import com.example.practicarecuadpspspringboot.repositories.CursoRepository;
import com.example.practicarecuadpspspringboot.services.CursoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(APIConfig.API_PATH + "/cursos")
public class CursoController {
    private final CursoRepository repository;
    private final CursoService service;
    private final CursoMapper mapper;

    @Autowired
    public CursoController(CursoRepository repository, CursoService service, CursoMapper mapper) {
        this.repository = repository;
        this.service = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "test", notes = "Test")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = String.class)})
    @GetMapping("/test")
    public String test(){
        return "CursoRepository.test() funcionando.";
    }

    @ApiOperation(value = "findAll", notes = "Finds all Cursos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not Found", response = CursosNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @GetMapping("/")
    public ResponseEntity<List<CursoDTO>> findAll(@RequestParam(required = false, name = "limit") Optional<String> limit,
                                                  @RequestParam(required = false, name = "name")Optional<String> name) {
        List<Curso> cursos = null;
        try {
            if (name.isPresent()) {
                cursos = service.findCursoByName(name.get()).orElse(null);
            } else {
                cursos = repository.findAll();
            }

            if (limit.isPresent() && !cursos.isEmpty() && cursos.size() > Integer.parseInt(limit.get())) {
                return ResponseEntity.ok(mapper.toDTO(cursos.subList(0, Integer.parseInt(limit.get()))));
            } else {
                if (!cursos.isEmpty()) {
                    return ResponseEntity.ok(mapper.toDTO(cursos));
                } else {
                    throw new CursosNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GenericBadRequestException("Data selection", "Incorrect parameters.");
        }
    }

    @ApiOperation(value = "Find Curso by id", notes = "Finds a Curso by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = CursoNotFoundException.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> findById(@PathVariable String id) {
        Curso curso = service.findCursoById(UUID.fromString(id)).orElse(null);
        if (curso == null) {
            throw new CursoNotFoundException(id);
        } else {
            return ResponseEntity.ok(mapper.toDTO(curso));
        }
    }

    @ApiOperation(value = "Find Curso by acronym", notes = "Finds a Curso by acronym")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = CursoNotFoundException.class)
    })
    @GetMapping("/acronym={acronym}")
    public ResponseEntity<CursoDTO> findByAcronym(@PathVariable String acronym) {
        Curso curso = service.findCursoByAcronym(acronym).orElse(null);
        if (curso == null) {
            throw new CursoNotFoundException(acronym);
        } else {
            return ResponseEntity.ok(mapper.toDTO(curso));
        }
    }

    @ApiOperation(value = "Save a Curso", notes = "Saves a Curso")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saved", response = CursoDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @PostMapping("/")
    public ResponseEntity<CursoDTO> save(@RequestBody CreateCursoDTO cursoDTO) {
        Curso curso = mapper.fromDTO(cursoDTO);
        checkData(curso);
        try {
            Curso insertedCurso = repository.save(curso);
            return ResponseEntity.ok(mapper.toDTO(insertedCurso));
        } catch (Exception e) {
            throw new GenericBadRequestException("Insert", "Unable to save Curso. Incorrect fields. --- "+e.getMessage());
        }
    }

    @ApiOperation(value = "Update a Curso", notes = "Updates a Curso with the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = CursoNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> update(@PathVariable String id, @RequestBody Curso curso) {
        Curso updatedCurso = repository.findById(UUID.fromString(id)).orElse(null);
        if (updatedCurso == null) {
            throw new CursoNotFoundException(id);
        }
        checkData(curso);

        updatedCurso.setName(curso.getName());
        updatedCurso.setNumber(curso.getNumber());
        updatedCurso.setAcronym(curso.getAcronym());
        updatedCurso.setModulos(curso.getModulos());
        try {
            updatedCurso = repository.save(updatedCurso);
            return ResponseEntity.ok(mapper.toDTO(updatedCurso));
        } catch (Exception e) {
            throw new GenericBadRequestException("Update", "Unable to update Curso. Incorrect fields. --- "+e.getMessage());
        }
    }

    @ApiOperation(value = "Delete a Curso", notes = "Deletes a Curso by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = CursoNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CursoDTO> delete(@PathVariable String id) {
        Curso curso = repository.findById(UUID.fromString(id)).orElse(null);
        if (curso == null) {
            throw new CursoNotFoundException(id);
        }
        try {
            repository.delete(curso);
            return ResponseEntity.ok(mapper.toDTO(curso));
        } catch (Exception e) {
            throw new GenericBadRequestException("Delete", "Unable to delete Curso. --- "+e.getMessage());
        }
    }

    private void checkData(Curso curso) {
        if (curso.getName() == null || curso.getName().isEmpty()) {
            throw new CursoBadRequestException("Name", "Name is mandatory.");
        }
        if (curso.getNumber() <= 0) {
            throw new CursoBadRequestException("Number", "Number must be at least 1.");
        }
        if (curso.getAcronym() == null || curso.getAcronym().isEmpty()) {
            throw new CursoBadRequestException("Acronym", "Acronym is mandatory.");
        }
    }

    @ApiOperation(value = "Finds all Curso in a paged list.", notes = "Returns a paged, ordered and filtered list.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ListCursoPageDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @GetMapping("/all")
    public ResponseEntity<?> pagedList(
            @RequestParam(required = false, name = "name") Optional<String> name,
            @RequestParam(required = false, name = "number") Optional<Integer> number,
            @RequestParam(required = false, name = "modulo") Optional<String> moduloAcronym,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Pageable paging = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Curso> pagedResult;

        try {
            if (name.isPresent() && number.isPresent() && moduloAcronym.isPresent()) {
                Page<Curso> unfilteredPageResult = repository.findByNameLikeIgnoreCaseAndNumber(name.get(), number.get(), paging).orElse(null);
                pagedResult = filterList(moduloAcronym, paging, unfilteredPageResult);
            } else if (name.isPresent()) {
                pagedResult = repository.findByNameLikeIgnoreCase(name.get(), paging).orElse(null);
            } else if (number.isPresent()) {
                pagedResult = repository.findByNumber(number.get(), paging).orElse(null);
            } else if (moduloAcronym.isPresent()) {
                Page<Curso> unfilteredPageResult = repository.findAll(paging);
                pagedResult = filterList(moduloAcronym, paging, unfilteredPageResult);
            } else {
                pagedResult = repository.findAll(paging);
            }

            ListCursoPageDTO lCursoPageDTO = ListCursoPageDTO.builder()
                    .data(mapper.toDTO(pagedResult.getContent()))
                    .totalPages(pagedResult.getTotalPages())
                    .totalElements(pagedResult.getTotalElements())
                    .currentPage(pagedResult.getNumber())
                    .sort(pagedResult.getSort().toString())
                    .build();
            return ResponseEntity.ok(lCursoPageDTO);
        } catch (Exception e) {
            throw new GenericBadRequestException("Data selection", "Incorrect parameters.");
        }
    }

    private Page<Curso> filterList(@RequestParam(name = "modulo") Optional<String> moduloAcronym, Pageable paging, Page<Curso> unfilteredPageResult) {
        ArrayList<Curso> filteredList = new ArrayList<>();
        for (Curso c : unfilteredPageResult) {
            boolean hasTheModulo = false;
            for (Modulo m : c.getModulos()) {
                if (m.getAcronym().contentEquals(moduloAcronym.get())) {
                    hasTheModulo = true;
                }
            }
            if (hasTheModulo) {
                filteredList.add(c);
            }
        }
        final int start = (int)paging.getOffset();
        final int end = Math.min((start + paging.getPageSize()), filteredList.size());
        return new PageImpl<>(filteredList.subList(start, end), paging, filteredList.size());
    }
}
