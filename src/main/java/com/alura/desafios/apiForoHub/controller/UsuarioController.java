package com.alura.desafios.apiForoHub.controller;

import com.alura.desafios.apiForoHub.domain.usuario.Usuario;
import com.alura.desafios.apiForoHub.domain.usuario.dto.ActualizarUsuarioDTO;
import com.alura.desafios.apiForoHub.domain.usuario.dto.DetallesUsuarioDTO;
import com.alura.desafios.apiForoHub.domain.usuario.repository.UsuarioRepository;
import com.alura.desafios.apiForoHub.domain.usuario.validations.actualizar.ValidarActualizarUsuario;
import com.alura.desafios.apiForoHub.domain.usuario.validations.crear.ValidarCrearUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Operaciones relacionadas con los usuarios, ")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    List<ValidarCrearUsuario> crearValidador;

    @Autowired
    List<ValidarActualizarUsuario> actualizarValidador;

    @GetMapping("/all")
    @Operation(summary = "Listar todos los usuarios", description = "Enumera todos los usuarios sin importar su estado.")
    public ResponseEntity<Page<DetallesUsuarioDTO>> leerTodosUsuarios(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = repository.findAll(pageable).map(DetallesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios habilitados", description = "Devuelve solo los usuarios que están habilitados.")
    public ResponseEntity<Page<DetallesUsuarioDTO>> leerUsuariosActivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = repository.findAllByEnabledTrue(pageable).map(DetallesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Buscar usuario por username", description = "Obtiene los datos de un usuario según su nombre de usuario.")
    public ResponseEntity<DetallesUsuarioDTO> leerUnUsuario(@PathVariable String username) {
        Usuario usuario = (Usuario) repository.findUsuarioByUsername(username);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene los datos de un usuario según su ID.")
    public ResponseEntity<DetallesUsuarioDTO> leerUnUsuario(@PathVariable Long id) {
        Usuario usuario = repository.getReferenceById(id);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    @Operation(summary = "Actualizar usuario", description = "Modifica datos de un usuario: contraseña, rol, nombre, apellido, email o estado habilitado.")
    public ResponseEntity<DetallesUsuarioDTO> actualizarUsuario(@RequestBody @Valid ActualizarUsuarioDTO actualizarUsuarioDTO, @PathVariable String username) {
        actualizarValidador.forEach(v -> v.validate(actualizarUsuarioDTO));

        Usuario usuario = (Usuario) repository.findUsuarioByUsername(username);

        if (actualizarUsuarioDTO.password() != null) {
            String hashedPassword = passwordEncoder.encode(actualizarUsuarioDTO.password());
            usuario.actualizarUsuarioConPassword(actualizarUsuarioDTO, hashedPassword);
        } else {
            usuario.actualizarUsuario(actualizarUsuarioDTO);
        }

        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    @Operation(summary = "Deshabilitar usuario", description = "Desactiva un usuario (eliminación lógica).")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String username) {
        Usuario usuario = (Usuario) repository.findUsuarioByUsername(username);
        usuario.eliminarUsuario();
        return ResponseEntity.noContent().build();
    }
}
