package com.alura.desafios.apiForoHub.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearUsuarioDTO(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 4, max = 20, message = "El username debe tener entre 4 y 20 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email válido")
        String email

) {}