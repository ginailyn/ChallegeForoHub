package com.alura.desafios.apiForoHub.controller;

import com.alura.desafios.apiForoHub.domain.curso.Curso;
import com.alura.desafios.apiForoHub.domain.curso.dto.DatosActualizarCursoDTO;
import com.alura.desafios.apiForoHub.domain.curso.dto.DatosDetalleCursoDTO;
import com.alura.desafios.apiForoHub.domain.curso.dto.DatosRegistrarCursoDTO;
import com.alura.desafios.apiForoHub.domain.curso.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "Operaciones relacionadas con los cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    @PostMapping
    @Operation(summary = "Registrar un nuevo curso", description = "Crea un nuevo curso en el sistema")
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistrarCursoDTO datos, UriComponentsBuilder uriComponentsBuilder) {
        var curso = new Curso(datos);
        cursoRepository.save(curso);

        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleCursoDTO(curso));
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos los cursos", description = "Devuelve una lista paginada de todos los cursos, estén activos o no")
    public ResponseEntity<Page<DatosDetalleCursoDTO>> listarCursos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = cursoRepository.findAll(pageable).map(DatosDetalleCursoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Listar cursos activos", description = "Devuelve una lista paginada de los cursos que están activos")
    public ResponseEntity<Page<DatosDetalleCursoDTO>> listarCursosActivos(@PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion) {
        var page = cursoRepository.findAllByActivoTrue(paginacion).map(DatosDetalleCursoDTO::new);
        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar curso", description = "Actualiza los datos de un curso existente usando su ID")
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizarCursoDTO datos) {
        var curso = cursoRepository.getReferenceById(datos.id());
        curso.actualizarInformacionCurso(datos);

        return ResponseEntity.ok(new DatosDetalleCursoDTO(curso));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar curso", description = "Marca un curso como inactivo (eliminación lógica)")
    public ResponseEntity eliminar(@PathVariable Long id) {
        var curso = cursoRepository.getReferenceById(id);
        curso.eliminar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de un curso", description = "Obtiene los detalles de un curso específico por su ID")
    public ResponseEntity detallar(@PathVariable Long id) {
        var curso = cursoRepository.getReferenceById(id);

        return ResponseEntity.ok(new DatosDetalleCursoDTO(curso));
    }
}