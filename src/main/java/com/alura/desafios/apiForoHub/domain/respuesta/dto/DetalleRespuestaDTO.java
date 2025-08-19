package com.alura.desafios.apiForoHub.domain.respuesta.dto;

import com.alura.desafios.apiForoHub.domain.respuesta.Respuesta;

import java.time.LocalDateTime;

public record DetalleRespuestaDTO(
        Long id,
        String mensaje,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion,
        Boolean solucion,
        Boolean borrado,
        Long usuarioId,
        String username,
        Long topicoId,
        String topico
) {

    public DetalleRespuestaDTO(Respuesta respuesta){
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario() != null ? respuesta.getUsuario().getId() : null,
                respuesta.getUsuario() != null ? respuesta.getUsuario().getUsername() : null,
                respuesta.getTopico() != null ? respuesta.getTopico().getId() : null,
                respuesta.getTopico() != null ? respuesta.getTopico().getTitulo() : null
        );
    }}
