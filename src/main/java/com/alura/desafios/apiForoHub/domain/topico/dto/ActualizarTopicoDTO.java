package com.alura.desafios.apiForoHub.domain.topico.dto;


import com.alura.desafios.apiForoHub.domain.topico.Estado;

public record ActualizarTopicoDTO(
        String titulo,
        String mensaje,
        Estado estado,
        Long cursoId
) {
}
