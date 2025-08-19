package com.alura.desafios.apiForoHub.domain.usuario.validations.crear;

import com.alura.desafios.apiForoHub.domain.usuario.dto.CrearUsuarioDTO;
public interface ValidarCrearUsuario {
    void validate(CrearUsuarioDTO data);
}