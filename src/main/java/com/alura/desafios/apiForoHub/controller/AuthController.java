package com.alura.desafios.apiForoHub.controller;

import com.alura.desafios.apiForoHub.domain.usuario.Usuario;
import com.alura.desafios.apiForoHub.domain.usuario.dto.CrearUsuarioDTO;
import com.alura.desafios.apiForoHub.domain.usuario.dto.DetallesUsuarioDTO;
import com.alura.desafios.apiForoHub.domain.usuario.dto.LoginDTO;
import com.alura.desafios.apiForoHub.domain.usuario.dto.LoginResponseDTO;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import com.alura.desafios.apiForoHub.domain.usuario.validations.crear.ValidarCrearUsuario;
import com.alura.desafios.apiForoHub.infra.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")

public class AuthController {


    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    List<ValidarCrearUsuario> crearValidador;

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Autenticar usuario",
            description = "Autentica un usuario existente y retorna un token JWT si las credenciales son válidas"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok(authService.autenticar(loginDTO));
    }


    @PostMapping("/register")
    @Transactional
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Registra un nuevo usuario en la base de datos con validaciones personalizadas"
    )
    public ResponseEntity<DetallesUsuarioDTO> crearUsuario(@RequestBody @Valid CrearUsuarioDTO crearUsuarioDTO, UriComponentsBuilder uriBuilder){
        crearValidador.forEach(v -> v.validate(crearUsuarioDTO));

        String hashedPassword = passwordEncoder.encode(crearUsuarioDTO.password());
        Usuario usuario = new Usuario(crearUsuarioDTO, hashedPassword);

        repository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getUsername()).toUri();
        return ResponseEntity.created(uri).body(new DetallesUsuarioDTO(usuario));
    }

}
