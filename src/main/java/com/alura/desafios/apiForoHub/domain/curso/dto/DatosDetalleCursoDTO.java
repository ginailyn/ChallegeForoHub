package com.alura.desafios.apiForoHub.domain.curso.dto;

import com.alura.desafios.apiForoHub.domain.curso.Categoria;
import com.alura.desafios.apiForoHub.domain.curso.Curso;

public record DatosDetalleCursoDTO (
        Long id,
        String nombre,
        Categoria categoria,
        Boolean activo

) {
public DatosDetalleCursoDTO(Curso curso) {
    this(
            curso.getId(),
            curso.getNombre(),
            curso.getCategoria(),
            curso.getActivo()
             );
}
}
