package com.alura.desafios.apiForoHub.controller;

import com.alura.desafios.apiForoHub.domain.respuesta.Respuesta;
import com.alura.desafios.apiForoHub.domain.respuesta.dto.ActualizarRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.respuesta.dto.CrearRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.respuesta.dto.DetalleRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.respuesta.repository.RespuestaRepository;
import com.alura.desafios.apiForoHub.domain.respuesta.validations.actualizar.ValidarRespuestaActualizada;
import com.alura.desafios.apiForoHub.domain.respuesta.validations.crear.ValidarRespuestaCreada;
import com.alura.desafios.apiForoHub.domain.topico.Estado;
import com.alura.desafios.apiForoHub.domain.topico.Topico;
import com.alura.desafios.apiForoHub.domain.topico.repository.TopicoRepository;
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
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuesta", description = "Operaciones relacionadas con las respuestas.")
public class RespuestaController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    List<ValidarRespuestaCreada> crearValidadores;

    @Autowired
    List<ValidarRespuestaActualizada> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Crear respuesta", description = "Crea una nueva respuesta asociada a un usuario y a un tópico.")
    public ResponseEntity<DetalleRespuestaDTO> crearRespuesta(@RequestBody @Valid CrearRespuestaDTO crearRespuestaDTO, UriComponentsBuilder uriBuilder) {
        crearValidadores.forEach(v -> v.validate(crearRespuestaDTO));

        Usuario usuario = usuarioRepository.getReferenceById(crearRespuestaDTO.usuarioId());
        Topico topico = topicoRepository.findById(crearRespuestaDTO.topicoId()).get();

        var respuesta = new Respuesta(crearRespuestaDTO, usuario, topico);
        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalleRespuestaDTO(respuesta));
    }

    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Listar respuestas de un tópico", description = "Obtiene todas las respuestas asociadas a un tópico específico.")
    public ResponseEntity<Page<DetalleRespuestaDTO>> leerRespuestaDeTopico(
            @PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable Long topicoId) {
        var pagina = respuestaRepository.findAllByTopicoId(topicoId, pageable).map(DetalleRespuestaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar respuestas de un usuario", description = "Obtiene todas las respuestas realizadas por un usuario específico.")
    public ResponseEntity<Page<DetalleRespuestaDTO>> leerRespuestasDeUsuarios(
            @PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable Long usuarioId) {
        var pagina = respuestaRepository.findAllByUsuarioId(usuarioId, pageable).map(DetalleRespuestaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de una respuesta", description = "Obtiene los detalles completos de una respuesta a partir de su ID.")
    public ResponseEntity<DetalleRespuestaDTO> leerUnaRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepository.getReferenceById(id);

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
    @Operation(summary = "Actualizar respuesta", description = "Actualiza el mensaje, el estado de solución o cualquier dato editable de una respuesta.")
    public ResponseEntity<DetalleRespuestaDTO> actualizarRespuesta(@RequestBody @Valid ActualizarRespuestaDTO actualizarRespuestaDTO, @PathVariable Long id) {
        actualizarValidadores.forEach(v -> v.validate(actualizarRespuestaDTO, id));
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.actualizarRespuesta(actualizarRespuestaDTO);

        if (actualizarRespuestaDTO.solucion()) {
            var temaResuelto = topicoRepository.getReferenceById(respuesta.getTopico().getId());
            temaResuelto.setEstado(Estado.CLOSED);
        }

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

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Eliminar respuesta", description = "Marca una respuesta como eliminada (eliminación lógica).")
    public ResponseEntity<Respuesta> borrarRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.eliminarRespuesta();
        return ResponseEntity.noContent().build();
    }
}

