package com.alura.desafios.apiForoHub.domain.topico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarTopicoDTO(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull Long usuarioId,
        @NotNull Long cursoId
) {
}
