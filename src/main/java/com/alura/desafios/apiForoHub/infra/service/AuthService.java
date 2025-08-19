package com.alura.desafios.apiForoHub.infra.service;

import com.alura.desafios.apiForoHub.domain.usuario.Usuario;
import com.alura.desafios.apiForoHub.domain.usuario.dto.LoginDTO;
import com.alura.desafios.apiForoHub.domain.usuario.dto.LoginResponseDTO;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import com.alura.desafios.apiForoHub.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO autenticar(LoginDTO loginDTO) {
        Usuario usuario = repository.findUsuarioByUsername(loginDTO.username());

        if (usuario == null || !passwordEncoder.matches(loginDTO.password(), usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        String tokenJWT = tokenService.generarToken(usuario.getUsername());
        // Devuelve solo datos seguros
        return new LoginResponseDTO(
                usuario.getUsername(),
                usuario.getRole().name(),
                tokenJWT

        );
    }
}