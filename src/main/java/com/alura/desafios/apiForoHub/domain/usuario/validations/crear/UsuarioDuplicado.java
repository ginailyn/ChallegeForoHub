package com.alura.desafios.apiForoHub.domain.usuario.validations.crear;

import com.alura.desafios.apiForoHub.domain.usuario.dto.CrearUsuarioDTO;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UsuarioDuplicado implements ValidarCrearUsuario {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(CrearUsuarioDTO data) {
        var usuarioDuplicado = repository.findUsuarioByUsername(data.username());
        if (usuarioDuplicado != null) {
            throw new ValidationException("Este usuario ya existe.");
        }

        var emailDuplicado = repository.findUsuarioByEmail(data.email());
        if (emailDuplicado != null) {
            throw new ValidationException("Este email ya existe.");
        }
    }
}