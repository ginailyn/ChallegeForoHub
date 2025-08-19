package com.alura.desafios.apiForoHub.domain.respuesta.validations.actualizar;

import com.alura.desafios.apiForoHub.domain.respuesta.Respuesta;
import com.alura.desafios.apiForoHub.domain.respuesta.dto.ActualizarRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.respuesta.repository.RespuestaRepository;
import com.alura.desafios.apiForoHub.domain.topico.Estado;
import com.alura.desafios.apiForoHub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolucionDuplicada implements ValidarRespuestaActualizada {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Override
    public void validate(ActualizarRespuestaDTO data, Long respuestaId) {
        if (Boolean.TRUE.equals(data.solucion())) {
            Respuesta respuesta = respuestaRepository.findById(respuestaId)
                    .orElseThrow(() -> new ValidationException("La respuesta no existe."));

            var topico = respuesta.getTopico();
            if (topico.getEstado() == Estado.CLOSED) {
                throw new ValidationException("Este tópico ya fue solucionado, no se puede marcar otra respuesta como solución.");
            }
        }
    }
}