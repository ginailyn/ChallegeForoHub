package com.alura.desafios.apiForoHub.domain.topico.validations.crear;

import com.alura.desafios.apiForoHub.domain.topico.dto.RegistrarTopicoDTO;
import com.alura.desafios.apiForoHub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicado implements ValidarTopicoCreado{

    @Autowired
    private TopicoRepository topicoRepository;


    @Override
    public void validate(RegistrarTopicoDTO data ) {
        var topicoDuplicado = topicoRepository.existsByTituloAndMensaje(data.titulo(), data.mensaje());
        if(topicoDuplicado){
            throw new ValidationException("Este topico ya existe. Revisa /topicos/" + topicoRepository.findByTitulo(data.titulo()).getId());

        }
    }
}
