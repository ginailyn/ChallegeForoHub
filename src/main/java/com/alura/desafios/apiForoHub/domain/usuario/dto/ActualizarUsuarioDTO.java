package com.alura.desafios.apiForoHub.domain.usuario.dto;

import com.alura.desafios.apiForoHub.domain.usuario.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioDTO(

        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        Role role,

        String nombre,

        String apellido,

        @Email(message = "El email debe tener un formato válido")
        String email,

        Boolean enabled

) {
}