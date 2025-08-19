package com.alura.desafios.apiForoHub.domain.usuario.validations.actualizar;

import com.alura.desafios.apiForoHub.domain.usuario.dto.ActualizarUsuarioDTO;

public interface ValidarActualizarUsuario {
    void validate(ActualizarUsuarioDTO data);
}