package com.example.practicarecuadpspspringboot;

import com.example.practicarecuadpspspringboot.model.Alumno;
import com.example.practicarecuadpspspringboot.model.Curso;
import com.example.practicarecuadpspspringboot.model.Modulo;
import com.example.practicarecuadpspspringboot.model.Performance;
import com.example.practicarecuadpspspringboot.repositories.AlumnoRepository;
import com.example.practicarecuadpspspringboot.repositories.CursoRepository;
import com.example.practicarecuadpspspringboot.repositories.ModuloRepository;
import com.example.practicarecuadpspspringboot.services.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class PracticaRecuAdPspSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticaRecuAdPspSpringBootApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(StorageService storageService) {
        return args -> {
            // storageService.deleteAll();
            storageService.init();
        };
    }

    @Bean
    public CommandLineRunner initCursosAndAlumnos(CursoRepository cursoRepo, ModuloRepository moduloRepo, AlumnoRepository alumnoRepo) {
        Set<String> idModulos1dam = Set.of(
                "modu0006-0000-0000-0000-000000000000",
                "modu0007-0000-0000-0000-000000000000",
                "modu0008-0000-0000-0000-000000000000",
                "modu0009-0000-0000-0000-000000000000"
        );
        Set<String> idModulos2dam = Set.of(
                "modu0001-0000-0000-0000-000000000000",
                "modu0002-0000-0000-0000-000000000000",
                "modu0003-0000-0000-0000-000000000000",
                "modu0004-0000-0000-0000-000000000000",
                "modu0005-0000-0000-0000-000000000000"
        );
        Set<Modulo> modulos1dam = getModulos(idModulos1dam, moduloRepo);
        Set<Modulo> modulos2dam = getModulos(idModulos2dam, moduloRepo);
        Curso dam1 = Curso.builder()
                .number(1)
                .name("Desarrollo de Aplicaciones Multiplataforma")
                .acronym("1DAM")
                .createdAt(LocalDateTime.now())
                .modulos(modulos1dam)
                .build();
        Curso dam2 = Curso.builder()
                .number(2)
                .name("Desarrollo de Aplicaciones Multiplataforma")
                .acronym("2DAM")
                .createdAt(LocalDateTime.now())
                .modulos(modulos2dam)
                .build();
        Alumno alumno1dam = Alumno.builder()
                .name("Alumno X de 1DAM")
                .email("alumnox@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .image("https://i.pinimg.com/236x/68/d2/5c/68d25cf6c159588b2e00d642dd62bfe8.jpg")
                .curso(dam1)
                .build();
        Alumno alumno2dam = Alumno.builder()
                .name("Alumno Y de 2DAM")
                .email("alumnoy@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .image("https://p16-sign.tiktokcdn-us.com/tos-useast5-avt-0068-tx/84428eae99c40b78a6d7dccd6805fc76~c5_720x720.jpeg?x-expires=1655024400&x-signature=kwR8FkjW8r93pDe2cZjY%2BYK4DJ4%3D")
                .curso(dam2)
                .build();
        alumno1dam.setModulos(getRandomPerformances(dam1.getModulos(), alumno1dam));
        alumno2dam.setModulos(getRandomPerformances(dam2.getModulos(), alumno2dam));
        return (args) -> {
            cursoRepo.save(dam1);
            cursoRepo.save(dam2);
            alumnoRepo.save(alumno1dam);
            alumnoRepo.save(alumno2dam);
        };
    }

    private Set<Performance> getRandomPerformances(Set<Modulo> modulos, Alumno alumno) {
        Set<Performance> result = new HashSet<>();
        for (Modulo modulo : modulos) {
            Performance p = Performance.builder()
                    .alumno(alumno)
                    .modulo(modulo)
                    .calificacion(Math.random()*10)
                    .build();
            result.add(p);
        }
        return result;
    }

    private Set<Modulo> getModulos(Set<String> idModulos, ModuloRepository repo) {
        Set<Modulo> result = new HashSet<>();
        for (String moduloStr : idModulos) {
            Modulo modulo = repo.findById(UUID.fromString(moduloStr)).orElse(null);
            if (modulo != null) {
                result.add(modulo);
            }
        }
        return result;
    }
}
