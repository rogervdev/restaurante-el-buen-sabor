package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoCartaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoCartaResponseDTO;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoCarta;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CatalogoPlatoCartaMapper {

    public CatalogoPlatoCarta toEntity(CatalogoPlatoCartaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return CatalogoPlatoCarta.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .stock(dto.getStock() != null ? dto.getStock() : 0)
                .activo(dto.getActivo() == null || dto.getActivo())
                .build();
    }

    public void updateEntity(CatalogoPlatoCarta plato, CatalogoPlatoCartaRequestDTO dto) {
        if (plato == null || dto == null) {
            return;
        }
        plato.setNombre(dto.getNombre());
        plato.setPrecio(dto.getPrecio());
        plato.setStock(dto.getStock());
        if (dto.getActivo() != null) {
            plato.setActivo(dto.getActivo());
        }
    }

    public CatalogoPlatoCartaResponseDTO toResponseDTO(CatalogoPlatoCarta plato) {
        if (plato == null) {
            return null;
        }
        return CatalogoPlatoCartaResponseDTO.builder()
                .id(plato.getId())
                .nombre(plato.getNombre())
                .precio(plato.getPrecio())
                .stock(plato.getStock())
                .activo(plato.getActivo())
                .build();
    }

    public List<CatalogoPlatoCartaResponseDTO> toResponseDTOList(List<CatalogoPlatoCarta> platos) {
        if (platos == null) {
            return List.of();
        }
        return platos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
