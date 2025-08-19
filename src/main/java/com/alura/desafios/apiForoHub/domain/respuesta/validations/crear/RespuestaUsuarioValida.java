package com.alura.desafios.apiForoHub.domain.respuesta.validations.crear;

import com.alura.desafios.apiForoHub.domain.respuesta.dto.CrearRespuestaDTO;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaUsuarioValida implements ValidarRespuestaCreada {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(CrearRespuestaDTO data) {
        var usuario = repository.findById(data.usuarioId())
                .orElseThrow(() -> new ValidationException("Este usuario no existe."));

        if (!usuario.isEnabled()) {
            throw new ValidationException("Este usuario no está habilitado.");
        }
    }
}