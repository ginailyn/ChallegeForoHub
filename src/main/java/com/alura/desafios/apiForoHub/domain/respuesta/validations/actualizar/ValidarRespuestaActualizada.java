package com.alura.desafios.apiForoHub.domain.respuesta.validations.actualizar;


import com.alura.desafios.apiForoHub.domain.respuesta.dto.ActualizarRespuestaDTO;

public interface ValidarRespuestaActualizada {

    void validate(ActualizarRespuestaDTO data, Long respuestaId);

}
