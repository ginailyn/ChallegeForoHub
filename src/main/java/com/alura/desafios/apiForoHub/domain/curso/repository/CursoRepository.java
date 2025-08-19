package com.alura.desafios.apiForoHub.domain.curso.repository;

import com.alura.desafios.apiForoHub.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository  extends JpaRepository<Curso, Long> {
    Page<Curso> findAllByActivoTrue(Pageable paginacion);
}
