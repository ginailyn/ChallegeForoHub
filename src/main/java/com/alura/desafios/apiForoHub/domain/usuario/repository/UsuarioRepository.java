package com.alura.desafios.apiForoHub.domain.usuario.repository;

import com.alura.desafios.apiForoHub.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioByUsername(String username);

    Usuario findUsuarioByEmail(String email);

    Page<Usuario> findAllByEnabledTrue(Pageable pageable);

    Usuario getReferenceById(Long id);

    Page<Usuario> findAll(Pageable pageable);

    Usuario getReferenceByUsername(String username);

    Boolean existsByUsername(String username);
}