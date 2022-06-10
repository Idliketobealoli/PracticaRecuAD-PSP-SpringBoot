package com.example.practicarecuadpspspringboot.controllers;

import com.example.practicarecuadpspspringboot.config.APIConfig;
import com.example.practicarecuadpspspringboot.dto.alumnoDTO.AlumnoDTO;
import com.example.practicarecuadpspspringboot.dto.alumnoDTO.CreateAlumnoDTO;
import com.example.practicarecuadpspspringboot.dto.alumnoDTO.ListAlumnoPageDTO;
import com.example.practicarecuadpspspringboot.errors.GenericBadRequestException;
import com.example.practicarecuadpspspringboot.errors.alumno.AlumnoBadRequestException;
import com.example.practicarecuadpspspringboot.errors.alumno.AlumnoNotFoundException;
import com.example.practicarecuadpspspringboot.errors.alumno.AlumnosNotFoundException;
import com.example.practicarecuadpspspringboot.mappers.AlumnoMapper;
import com.example.practicarecuadpspspringboot.model.Alumno;
import com.example.practicarecuadpspspringboot.model.Performance;
import com.example.practicarecuadpspspringboot.repositories.AlumnoRepository;
import com.example.practicarecuadpspspringboot.services.AlumnoService;
import com.example.practicarecuadpspspringboot.services.storage.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(APIConfig.API_PATH + "/alumnos")
public class AlumnoController {
    private final AlumnoRepository repository;
    private final StorageService storageService;
    private final AlumnoService service;
    private final AlumnoMapper mapper;

    @Autowired
    public AlumnoController(AlumnoRepository repository, StorageService storageService, AlumnoService service, AlumnoMapper mapper) {
        this.repository = repository;
        this.storageService = storageService;
        this.service = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "test", notes = "Test")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = String.class)})
    @GetMapping("/test")
    public String test(){
        return "AlumnoRepository.test() funcionando.";
    }

    @ApiOperation(value = "findAll", notes = "Finds all Alumnos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AlumnoDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not Found", response = AlumnosNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @GetMapping("/")
    public ResponseEntity<List<AlumnoDTO>> findAll(@RequestParam(required = false, name = "limit")Optional<String> limit,
                                                   @RequestParam(required = false, name = "name")Optional<String> name) {
        List<Alumno> alumnos = null;
        try {
            if (name.isPresent()) {
                alumnos = service.findAlumnoByName(name.get());
            } else {
                alumnos = repository.findAll();
            }

            if (limit.isPresent() && !alumnos.isEmpty() && alumnos.size() > Integer.parseInt(limit.get())) {
                return ResponseEntity.ok(mapper.toDTO(alumnos.subList(0, Integer.parseInt(limit.get()))));
            } else {
                if (!alumnos.isEmpty()) {
                    return ResponseEntity.ok(mapper.toDTO(alumnos));
                } else {
                    throw new AlumnosNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GenericBadRequestException("Data selection", "Incorrect parameters.");
        }
    }

    @ApiOperation(value = "Find Alumno by id", notes = "Finds an Alumno by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AlumnoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = AlumnoNotFoundException.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> findById(@PathVariable String id) {
        Alumno alumno = service.findAlumnoById(UUID.fromString(id)).orElse(null);
        if (alumno == null) {
            throw new AlumnoNotFoundException(id);
        } else {
            return ResponseEntity.ok(mapper.toDTO(alumno));
        }
    }

    @ApiOperation(value = "Find Alumno by email", notes = "Finds an Alumno by email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AlumnoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = AlumnoNotFoundException.class)
    })
    @GetMapping("/email={email}")
    public ResponseEntity<AlumnoDTO> findByEmail(@PathVariable String email) {
        Alumno alumno = service.findAlumnoByEmail(email).orElse(null);
        if (alumno == null) {
            throw new AlumnoNotFoundException(email);
        } else {
            return ResponseEntity.ok(mapper.toDTO(alumno));
        }
    }

    @ApiOperation(value = "Save an Alumno", notes = "Saves an Alumno")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saved", response = AlumnoDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @PostMapping("/")
    public ResponseEntity<AlumnoDTO> save(@RequestBody CreateAlumnoDTO alumnoDTO) {
        Alumno alumno = mapper.fromDTO(alumnoDTO);
        checkData(alumno);
        try {
            Alumno insertedAlumno = repository.save(alumno);
            return ResponseEntity.ok(mapper.toDTO(insertedAlumno));
        } catch (Exception e) {
            throw new GenericBadRequestException("Insert", "Unable to save Alumno. Incorrect fields. --- "+e.getMessage());
        }
    }

    @ApiOperation(value = "Update an Alumno", notes = "Updates an Alumno with the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AlumnoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = AlumnoNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDTO> update(@PathVariable String id, @RequestBody Alumno alumno) {
        Alumno updatedAlumno = repository.findById(UUID.fromString(id)).orElse(null);
        if (updatedAlumno == null) {
            throw new AlumnoNotFoundException(id);
        }
        checkData(alumno);

        updatedAlumno.setName(alumno.getName());
        // No se podrá cambiar el email ni la foto una vez se cree el alumno. Decisión de diseño.
        // updatedAlumno.setEmail(alumno.getEmail());
        // updatedAlumno.setImage(alumno.getImage());
        updatedAlumno.setCurso(alumno.getCurso());
        updatedAlumno.setModulos(alumno.getModulos());
        try {
            updatedAlumno = repository.save(updatedAlumno);
            return ResponseEntity.ok(mapper.toDTO(updatedAlumno));
        } catch (Exception e) {
            throw new GenericBadRequestException("Update", "Unable to update Alumno. Incorrect fields. --- "+e.getMessage());
        }
    }

    @ApiOperation(value = "Delete an Alumno", notes = "Deletes an Alumno by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AlumnoDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = AlumnoNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AlumnoDTO> delete(@PathVariable String id) {
        Alumno alumno = repository.findById(UUID.fromString(id)).orElse(null);
        if (alumno == null) {
            throw new AlumnoNotFoundException(id);
        }
        try {
            repository.delete(alumno);
            return ResponseEntity.ok(mapper.toDTO(alumno));
        } catch (Exception e) {
            throw new GenericBadRequestException("Delete", "Unable to delete Alumno. --- "+e.getMessage());
        }
    }

    private void checkData(Alumno alumno) {
        if (alumno.getName() == null || alumno.getName().isEmpty()) {
            throw new AlumnoBadRequestException("Name", "Name is mandatory.");
        }
        if (alumno.getEmail() == null || !alumno.getEmail().matches(".*@.*\\..*")) {
            throw new AlumnoBadRequestException("Email", "Email is incorrect or nonexistent.");
        }
    }

    @ApiOperation(value = "Creates a new Alumno with an image", notes = "Creates a new Alumno with an image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AlumnoDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class),
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> newAlumno(
            @RequestPart("alumno") CreateAlumnoDTO alumnoDTO,
            @RequestPart("file") MultipartFile file) {
        Alumno alumno = mapper.fromDTO(alumnoDTO);
        checkData(alumno);

        if (!file.isEmpty()) {
            String image = storageService.store(file);
            String imageURL = storageService.getUrl(image);
            alumno.setImage(imageURL);
        }

        try {
            Alumno savedAlumno = repository.save(alumno);
            return ResponseEntity.ok(mapper.toDTO(savedAlumno));
        } catch (AlumnoNotFoundException e) {
            throw new GenericBadRequestException("Insert", "Could not insert Alumno; incorrect fields.");
        }
    }

    @ApiOperation(value = "Finds all Alumno in a paged list.", notes = "Returns a paged, ordered and filtered list.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ListAlumnoPageDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GenericBadRequestException.class)
    })
    @GetMapping("/all")
    public ResponseEntity<?> pagedList(
            @RequestParam(required = false, name = "name") Optional<String> name,
            @RequestParam(required = false, name = "media") Optional<Double> media,
            @RequestParam(required = false, name = "curso") Optional<String> cursoAcronym,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Pageable paging = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Alumno> pagedResult;

        try {
            if (name.isPresent() && media.isPresent() && cursoAcronym.isPresent()) {
                Page<Alumno> unfilteredPageResult = repository.findByNameContainsIgnoreCaseAndCurso_Acronym(name.get(), cursoAcronym.get(), paging);
                pagedResult = filterList(media, paging, unfilteredPageResult);
            } else if (name.isPresent()) {
                pagedResult = repository.findByNameLikeIgnoreCase(name.get(), paging);
            } else if (cursoAcronym.isPresent()) {
                pagedResult = repository.findByCurso_Acronym(cursoAcronym.get(), paging);
            } else if (media.isPresent()) {
                Page<Alumno> unfilteredPageResult = repository.findAll(paging);
                pagedResult = filterList(media, paging, unfilteredPageResult);
            } else {
                pagedResult = repository.findAll(paging);
            }

            ListAlumnoPageDTO lAlumnoPageDTO = ListAlumnoPageDTO.builder()
                    .data(mapper.toDTO(pagedResult.getContent()))
                    .totalPages(pagedResult.getTotalPages())
                    .totalElements(pagedResult.getTotalElements())
                    .currentPage(pagedResult.getNumber())
                    .sort(pagedResult.getSort().toString())
                    .build();
            return ResponseEntity.ok(lAlumnoPageDTO);
        } catch (Exception e) {
            throw new GenericBadRequestException("Data selection", "Incorrect parameters.");
        }
    }

    private Page<Alumno> filterList(@RequestParam(name = "media") Optional<Double> media, Pageable paging, Page<Alumno> unfilteredPageResult) {
        ArrayList<Alumno> filteredList = new ArrayList<>();
        for (Alumno a : unfilteredPageResult) {
            double totalScore = 0;
            for (Performance p : a.getModulos()) {
                totalScore += p.getCalificacion();
            }
            double aMedia = totalScore / a.getModulos().size();
            if (aMedia >= media.get()) {
                filteredList.add(a);
            }
        }
        final int start = (int)paging.getOffset();
        final int end = Math.min((start + paging.getPageSize()), filteredList.size());
        return new PageImpl<>(filteredList.subList(start, end), paging, filteredList.size());
    }
}