package com.alura.desafios.apiForoHub.domain.usuario.dto;

public record LoginResponseDTO(
        String username,
        String role,
        String token // opcional si luego usas JWT
) {}
