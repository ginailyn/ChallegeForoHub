package com.alura.desafios.apiForoHub.domain.topico.validations.crear;

import com.alura.desafios.apiForoHub.domain.topico.dto.RegistrarTopicoDTO;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoUsuario implements ValidarTopicoCreado {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(RegistrarTopicoDTO data) {
        var usuario = repository.findById(data.usuarioId())
                .orElseThrow(() -> new ValidationException("Este usuario no existe."));

        if (!usuario.getEnabled()) {
            throw new ValidationException("Este usuario fue deshabilitado.");
        }
    }
}