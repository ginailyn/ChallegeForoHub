package com.alura.desafios.apiForoHub.infra.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web de Spring Security
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter; // Nuestro filtro personalizado para JWT

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Deshabilitamos CSRF ya que no usamos sesiones tradicionales
                .csrf(csrf -> csrf.disable())

                // Configuramos la política de sesión: sin estado (stateless), ideal para JWT
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuramos permisos de acceso a los endpoints
                .authorizeHttpRequests(req -> {

                    // Permitir Swagger UI y recursos estáticos
                req.requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/webjars/**",
                            "/swagger-resources/**"
                    ).permitAll();
                    // Permitimos que cualquiera haga POST al endpoint de login
                    req.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/v3/api-docs").permitAll();

                    // Para todos los demás endpoints se requiere autenticación
                    req.anyRequest().authenticated();
                })

                // Añadimos nuestro filtro personalizado antes del filtro de autenticación por usuario y contraseña
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Bean que expone el AuthenticationManager para poder usarlo en otros servicios (como en login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Bean para codificar las contraseñas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}