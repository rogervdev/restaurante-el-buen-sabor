package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.PresaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import com.elbuensabor.restaurantapi.mapper.PresaMapper;
import com.elbuensabor.restaurantapi.model.Presa;
import com.elbuensabor.restaurantapi.repository.PresaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresaService {

    private final PresaRepository presaRepository;
    private final PresaMapper presaMapper;

    public List<PresaResponseDTO> listarTodas() {
        return presaMapper.toResponseDTOList(presaRepository.findAll());
    }

    public PresaResponseDTO obtenerPorId(Long id) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presa no encontrada con ID: " + id));
        return presaMapper.toResponseDTO(presa);
    }

    public PresaResponseDTO crear(PresaRequestDTO request) {
        Presa presa = presaMapper.toEntity(request);
        Presa presaGuardada = presaRepository.save(presa);
        return presaMapper.toResponseDTO(presaGuardada);
    }

    public PresaResponseDTO actualizar(Long id, PresaRequestDTO request) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presa no encontrada con ID: " + id));

        presaMapper.updateEntity(presa, request);

        Presa presaActualizada = presaRepository.save(presa);
        return presaMapper.toResponseDTO(presaActualizada);
    }

    @Transactional
    public PresaResponseDTO deshabilitar(Long id) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presa no encontrada con ID: " + id));

        presa.setActivo(false);
        Presa presaActualizada = presaRepository.save(presa);

        return presaMapper.toResponseDTO(presaActualizada);
    }

    @Transactional
    public PresaResponseDTO activar(Long id) {
        Presa presa = presaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presa no encontrada con ID: " + id));

        presa.setActivo(true);
        Presa presaActualizada = presaRepository.save(presa);

        return presaMapper.toResponseDTO(presaActualizada);
    }
}
