package com.alura.desafios.apiForoHub.domain.curso;


import com.alura.desafios.apiForoHub.domain.curso.dto.DatosActualizarCursoDTO;
import com.alura.desafios.apiForoHub.domain.curso.dto.DatosRegistrarCursoDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Boolean activo;

    public Curso(DatosRegistrarCursoDTO datosRegistrarCursoDTO){
        this.nombre = datosRegistrarCursoDTO.nombre();
        this.categoria = datosRegistrarCursoDTO.categoria();
        this.activo = true; // Siempre debería comenzar estando activo el curso
    }


    public void actualizarInformacionCurso(@Valid DatosActualizarCursoDTO datos) {
        if (datos.nombre() != null) {
            this.nombre = datos.nombre();
        }
        if (datos.activo() != null) {
            this.activo= datos.activo();
        }
        if (datos.categoria() != null) {
            this.categoria= datos.categoria();
        }

    }
    public void eliminar() {
        this.activo = false;
    }


}
