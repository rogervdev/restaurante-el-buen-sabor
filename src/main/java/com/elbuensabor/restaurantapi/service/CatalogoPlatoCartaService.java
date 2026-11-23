package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoCartaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoCartaResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import com.elbuensabor.restaurantapi.mapper.CatalogoPlatoCartaMapper;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoCarta;
import com.elbuensabor.restaurantapi.repository.CatalogoPlatoCartaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoPlatoCartaService {

    private final CatalogoPlatoCartaRepository catalogoPlatoCartaRepository;
    private final CatalogoPlatoCartaMapper catalogoPlatoCartaMapper;

    public List<CatalogoPlatoCartaResponseDTO> listarTodos() {
        return catalogoPlatoCartaMapper.toResponseDTOList(catalogoPlatoCartaRepository.findAll());
    }

    public CatalogoPlatoCartaResponseDTO obtenerPorId(Long id) {
        CatalogoPlatoCarta plato = catalogoPlatoCartaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato a la carta no encontrado con ID: " + id));
        return catalogoPlatoCartaMapper.toResponseDTO(plato);
    }

    public CatalogoPlatoCartaResponseDTO crear(CatalogoPlatoCartaRequestDTO request) {
        CatalogoPlatoCarta plato = catalogoPlatoCartaMapper.toEntity(request);
        CatalogoPlatoCarta platoGuardado = catalogoPlatoCartaRepository.save(plato);
        return catalogoPlatoCartaMapper.toResponseDTO(platoGuardado);
    }

    public CatalogoPlatoCartaResponseDTO actualizar(Long id, CatalogoPlatoCartaRequestDTO request) {
        CatalogoPlatoCarta plato = catalogoPlatoCartaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato a la carta no encontrado con ID: " + id));

        catalogoPlatoCartaMapper.updateEntity(plato, request);

        CatalogoPlatoCarta platoActualizado = catalogoPlatoCartaRepository.save(plato);
        return catalogoPlatoCartaMapper.toResponseDTO(platoActualizado);
    }

    public void eliminar(Long id) {
        if (!catalogoPlatoCartaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plato a la carta no encontrado con ID: " + id);
        }
        catalogoPlatoCartaRepository.deleteById(id);
    }
}
