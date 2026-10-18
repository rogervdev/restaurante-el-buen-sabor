package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.PresaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.model.Presa;
import com.elbuensabor.restaurantapi.repository.PresaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresaService {

    private final PresaRepository presaRepository;

    public List<PresaResponseDTO> listarTodas() {
        return presaRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public PresaResponseDTO obtenerPorId(Long id) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + id));
        return convertirAResponseDTO(presa);
    }

    public PresaResponseDTO crear(PresaRequestDTO request) {
        Presa presa = new Presa();
        presa.setNombre(request.getNombre());
        presa.setPrecioAdicional(request.getPrecioAdicional());
        presa.setActivo(true);
        Presa presaGuardada = presaRepository.save(presa);
        return convertirAResponseDTO(presaGuardada);
    }

    public PresaResponseDTO actualizar(Long id, PresaRequestDTO request) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + id));
        presa.setNombre(request.getNombre());
        presa.setPrecioAdicional(request.getPrecioAdicional());
        Presa presaActualizada = presaRepository.save(presa);
        return convertirAResponseDTO(presaActualizada);
    }

    @Transactional
    public PresaResponseDTO deshabilitar(Long id) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + id));

        presa.setActivo(false);
        Presa presaActualizada = presaRepository.save(presa);

        return convertirAResponseDTO(presaActualizada);
    }

    // Opcional: Método para reactivar
    @Transactional
    public PresaResponseDTO activar(Long id) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + id));

        presa.setActivo(true);
        Presa presaActualizada = presaRepository.save(presa);

        return convertirAResponseDTO(presaActualizada);
    }

    private PresaResponseDTO convertirAResponseDTO(Presa presa) {
        return PresaResponseDTO.builder()
                .id(presa.getId())
                .nombre(presa.getNombre())
                .precioAdicional(presa.getPrecioAdicional())
                .activo(presa.getActivo())
                .build();
    }
}