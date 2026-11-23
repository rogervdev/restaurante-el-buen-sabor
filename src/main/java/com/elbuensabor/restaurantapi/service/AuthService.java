package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.LoginRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.LoginResponseDTO;
import com.elbuensabor.restaurantapi.model.Usuario;
import com.elbuensabor.restaurantapi.repository.UsuarioRepository;
import com.elbuensabor.restaurantapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("El correo electrónico no está registrado"));

        if (!usuario.isActivo()) {
            throw new IllegalStateException("La cuenta está desactivada. Contacte al administrador.");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(usuario);

        return LoginResponseDTO.builder()
                .token(token)
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol() != null ? usuario.getRol().name() : null)
                .build();
    }
}
