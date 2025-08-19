package com.alura.desafios.apiForoHub.domain.respuesta.validations.crear;

import com.alura.desafios.apiForoHub.domain.respuesta.dto.CrearRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.topico.Estado;
import com.alura.desafios.apiForoHub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaTopicoValida implements ValidarRespuestaCreada {

    @Autowired
    private TopicoRepository repository;

    @Override
    public void validate(CrearRespuestaDTO data) {
        var topico = repository.findById(data.topicoId())
                .orElseThrow(() -> new ValidationException("Este tópico no existe."));

        if (topico.getEstado() != Estado.OPEN) {
            throw new ValidationException("Este tópico no está abierto.");
        }
    }
}