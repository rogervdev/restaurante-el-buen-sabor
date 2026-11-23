package com.elbuensabor.restaurantapi.config;

import com.elbuensabor.restaurantapi.model.Rol;
import com.elbuensabor.restaurantapi.model.Usuario;
import com.elbuensabor.restaurantapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@elbuensabor.com";
    private static final String ADMIN_PASSWORD = "Admin123*";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador")
                    .email(ADMIN_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .rol(Rol.ADMIN)
                    .activo(true)
                    .build();

            usuarioRepository.save(admin);
            log.info("Usuario administrador por defecto creado: {}", ADMIN_EMAIL);
        }
    }
}
