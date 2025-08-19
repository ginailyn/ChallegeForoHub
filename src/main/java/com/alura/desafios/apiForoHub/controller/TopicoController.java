package com.alura.desafios.apiForoHub.controller;

import com.alura.desafios.apiForoHub.domain.curso.Curso;
import com.alura.desafios.apiForoHub.domain.curso.repository.CursoRepository;
import com.alura.desafios.apiForoHub.domain.respuesta.Respuesta;
import com.alura.desafios.apiForoHub.domain.respuesta.dto.DetalleRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.respuesta.repository.RespuestaRepository;
import com.alura.desafios.apiForoHub.domain.topico.Estado;
import com.alura.desafios.apiForoHub.domain.topico.Topico;
import com.alura.desafios.apiForoHub.domain.topico.dto.ActualizarTopicoDTO;
import com.alura.desafios.apiForoHub.domain.topico.dto.DetallesTopicoDTO;
import com.alura.desafios.apiForoHub.domain.topico.dto.RegistrarTopicoDTO;
import com.alura.desafios.apiForoHub.domain.topico.repository.TopicoRepository;
import com.alura.desafios.apiForoHub.domain.topico.validations.actualizar.ValidarTopicoActualizado;
import com.alura.desafios.apiForoHub.domain.topico.validations.crear.ValidarTopicoCreado;
import com.alura.desafios.apiForoHub.domain.usuario.Usuario;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topico", description = "Operaciones relacionadas a Tópicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    RespuestaRepository respuestaRepository;

    @Autowired
    List<ValidarTopicoCreado> crearValidadores;

    @Autowired
    List<ValidarTopicoActualizado> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Crear tópico", description = "Registra un nuevo tópico vinculado a un curso y usuario.")
    public ResponseEntity<DetallesTopicoDTO> crearTopico(@RequestBody @Valid RegistrarTopicoDTO crearTopicoDTO, UriComponentsBuilder uriBuilder) {
        crearValidadores.forEach(v -> v.validate(crearTopicoDTO));

        Usuario usuario = usuarioRepository.findById(crearTopicoDTO.usuarioId()).get();
        Curso curso = cursoRepository.findById(crearTopicoDTO.cursoId()).get();
        Topico topico = new Topico(crearTopicoDTO, usuario, curso);

        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetallesTopicoDTO(topico));
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos los tópicos", description = "Lista todos los tópicos sin importar su estado.")
    public ResponseEntity<Page<DetallesTopicoDTO>> leerTodosTopicos(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var pagina = topicoRepository.findAll(pageable).map(DetallesTopicoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Listar tópicos activos", description = "Lista todos los tópicos que no están eliminados.")
    public ResponseEntity<Page<DetallesTopicoDTO>> leerTopicosNoEliminados(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var pagina = topicoRepository.findAllByEstadoIsNot(Estado.DELETED, pageable).map(DetallesTopicoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Leer un tópico", description = "Obtiene los detalles de un tópico específico por su ID.")
    public ResponseEntity<DetallesTopicoDTO> leerUnTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DetallesTopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @GetMapping("/{id}/solucion")
    @Operation(summary = "Obtener solución del tópico", description = "Obtiene la respuesta marcada como solución para el tópico dado.")
    public ResponseEntity<DetalleRespuestaDTO> leerSolucionTopico(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepository.getReferenceByTopicoId(id);

        var datosRespuesta = new DetalleRespuestaDTO(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualizar tópico", description = "Actualiza el título, mensaje, estado o curso asociado a un tópico.")
    public ResponseEntity<DetallesTopicoDTO> actualizarTopico(@RequestBody @Valid ActualizarTopicoDTO actualizarTopicoDTO, @PathVariable Long id) {
        actualizarValidadores.forEach(v -> v.validate(actualizarTopicoDTO));

        Topico topico = topicoRepository.getReferenceById(id);

        if (actualizarTopicoDTO.cursoId() != null) {
            Curso curso = cursoRepository.getReferenceById(actualizarTopicoDTO.cursoId());
            topico.actualizarTopicoConCurso(actualizarTopicoDTO, curso);
        } else {
            topico.actualizarTopico(actualizarTopicoDTO);
        }

        var datosTopico = new DetallesTopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Eliminar tópico", description = "Marca un tópico como eliminado (eliminación lógica).")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.eliminarTopico();
        return ResponseEntity.noContent().build();
    }
}
