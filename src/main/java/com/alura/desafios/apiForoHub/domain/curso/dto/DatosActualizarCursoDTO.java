package com.alura.desafios.apiForoHub.domain.curso.dto;

import com.alura.desafios.apiForoHub.domain.curso.Categoria;
import jakarta.validation.constraints.NotNull;

public record DatosActualizarCursoDTO(
    @NotNull Long id,
    String nombre,
    Boolean activo,
    Categoria categoria
)
{
}
