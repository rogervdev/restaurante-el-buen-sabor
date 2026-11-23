package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.request.UsuarioRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.UsuarioResponseDTO;
import com.elbuensabor.restaurantapi.model.Rol;
import com.elbuensabor.restaurantapi.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto, String encodedPassword) {
        if (dto == null) {
            return null;
        }
        return Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .dni(normalizar(dto.getDni()))
                .telefono(normalizar(dto.getTelefono()))
                .direccion(normalizar(dto.getDireccion()))
                .password(encodedPassword)
                .rol(parseRol(dto.getRol()))
                .activo(true)
                .build();
    }

    public void updateEntity(Usuario usuario, UsuarioRequestDTO dto) {
        if (usuario == null || dto == null) {
            return;
        }
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setDni(normalizar(dto.getDni()));
        usuario.setTelefono(normalizar(dto.getTelefono()));
        usuario.setDireccion(normalizar(dto.getDireccion()));
        usuario.setRol(parseRol(dto.getRol()));
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .dni(usuario.getDni())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .rol(usuario.getRol() != null ? usuario.getRol().name() : null)
                .activo(usuario.isActivo())
                .build();
    }

    public List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return List.of();
        }
        return usuarios.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Rol parseRol(String rol) {
        return rol != null ? Rol.valueOf(rol.toUpperCase()) : null;
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
