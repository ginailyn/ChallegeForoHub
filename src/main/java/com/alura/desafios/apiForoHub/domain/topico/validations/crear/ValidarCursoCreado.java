package com.alura.desafios.apiForoHub.domain.topico.validations.crear;

import com.alura.desafios.apiForoHub.domain.curso.repository.CursoRepository;
import com.alura.desafios.apiForoHub.domain.topico.dto.RegistrarTopicoDTO;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCursoCreado implements ValidarTopicoCreado {

    @Autowired
    private CursoRepository repository;

    @Override
    public void validate(RegistrarTopicoDTO data) {
        var curso = repository.findById(data.cursoId())
                .orElseThrow(() -> new ValidationException("Este curso no existe."));

        if (!curso.getActivo()) {
            throw new ValidationException("Este curso no está disponible en este momento.");
        }
    }
}