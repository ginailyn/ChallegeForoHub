package com.alura.desafios.apiForoHub.domain.curso.dto;

import com.alura.desafios.apiForoHub.domain.curso.Categoria;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistrarCursoDTO(
    @NotBlank String nombre,
    Categoria categoria){
}
