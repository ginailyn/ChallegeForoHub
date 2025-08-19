package com.alura.desafios.apiForoHub.controller;

import com.alura.desafios.apiForoHub.domain.usuario.Usuario;
import com.alura.desafios.apiForoHub.domain.usuario.dto.DatosAutenticacion;
import com.alura.desafios.apiForoHub.infra.security.DatosTokenJWT;
import com.alura.desafios.apiForoHub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity<DatosTokenJWT> iniciarSesion(@RequestBody @Valid DatosAutenticacion datos) {
        var authToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.contrasena());
        var autenticacion = manager.authenticate(authToken);

        // Obtener el usuario autenticado
        var usuario = (Usuario) autenticacion.getPrincipal();

        // Generar token con username
        var tokenJWT = tokenService.generarToken(usuario.getUsername());

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}