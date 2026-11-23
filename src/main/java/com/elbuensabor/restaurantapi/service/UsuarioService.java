package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.UsuarioRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.UsuarioResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import com.elbuensabor.restaurantapi.mapper.UsuarioMapper;
import com.elbuensabor.restaurantapi.model.Usuario;
import com.elbuensabor.restaurantapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listar() {
        return usuarioMapper.toResponseDTOList(usuarioRepository.findAll());
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.getEmail());
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria para crear un usuario");
        }
        validarDni(request.getDni(), null);

        Usuario usuario = usuarioMapper.toEntity(request, passwordEncoder.encode(request.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (usuarioRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.getEmail());
        }
        validarDni(request.getDni(), id);

        usuarioMapper.updateEntity(usuario, request);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    public UsuarioResponseDTO cambiarEstado(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(!usuario.isActivo());
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    /**
     * El DNI es opcional; si viene informado debe tener exactamente 8 dígitos
     * numéricos y no puede pertenecer a otro usuario.
     */
    private void validarDni(String dni, Long idExcluir) {
        if (dni == null || dni.isBlank()) {
            return;
        }

        String dniNormalizado = dni.trim();
        if (!dniNormalizado.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos numéricos");
        }

        boolean duplicado = idExcluir == null
                ? usuarioRepository.existsByDni(dniNormalizado)
                : usuarioRepository.existsByDniAndIdNot(dniNormalizado, idExcluir);

        if (duplicado) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI: " + dniNormalizado);
        }
    }
}
