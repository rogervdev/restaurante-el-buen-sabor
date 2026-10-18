package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoCartaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoCartaResponseDTO;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoCarta;
import com.elbuensabor.restaurantapi.repository.CatalogoPlatoCartaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoPlatoCartaService {

    private final CatalogoPlatoCartaRepository catalogoPlatoCartaRepository;

    public List<CatalogoPlatoCartaResponseDTO> listarTodos() {
        return catalogoPlatoCartaRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public CatalogoPlatoCartaResponseDTO obtenerPorId(Long id) {
        CatalogoPlatoCarta plato = catalogoPlatoCartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato a la carta no encontrado con ID: " + id));
        return convertirAResponseDTO(plato);
    }

    public CatalogoPlatoCartaResponseDTO crear(CatalogoPlatoCartaRequestDTO request) {
        CatalogoPlatoCarta plato = new CatalogoPlatoCarta();
        plato.setNombre(request.getNombre());
        plato.setCategoria(CatalogoPlatoCarta.Categoria.valueOf(request.getCategoria()));
        plato.setPrecio(request.getPrecio());
        plato.setStock(request.getStock());
        plato.setActivo(true);
        CatalogoPlatoCarta platoGuardado = catalogoPlatoCartaRepository.save(plato);
        return convertirAResponseDTO(platoGuardado);
    }

    public CatalogoPlatoCartaResponseDTO actualizar(Long id, CatalogoPlatoCartaRequestDTO request) {
        CatalogoPlatoCarta plato = catalogoPlatoCartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato a la carta no encontrado con ID: " + id));
        plato.setNombre(request.getNombre());
        plato.setCategoria(CatalogoPlatoCarta.Categoria.valueOf(request.getCategoria()));
        plato.setPrecio(request.getPrecio());
        plato.setStock(request.getStock());
        CatalogoPlatoCarta platoActualizado = catalogoPlatoCartaRepository.save(plato);
        return convertirAResponseDTO(platoActualizado);
    }

    public void eliminar(Long id) {
        if (!catalogoPlatoCartaRepository.existsById(id)) {
            throw new RuntimeException("Plato a la carta no encontrado con ID: " + id);
        }
        catalogoPlatoCartaRepository.deleteById(id);
    }

    private CatalogoPlatoCartaResponseDTO convertirAResponseDTO(CatalogoPlatoCarta plato) {
        return CatalogoPlatoCartaResponseDTO.builder()
                .id(plato.getId())
                .nombre(plato.getNombre())
                .categoria(plato.getCategoria().name())
                .precio(plato.getPrecio())
                .stock(plato.getStock())
                .activo(plato.getActivo())
                .build();
    }
}