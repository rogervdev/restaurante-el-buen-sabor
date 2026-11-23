package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.request.PresaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.model.Presa;
import com.elbuensabor.restaurantapi.model.PresaDia;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PresaMapper {

    public Presa toEntity(PresaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Presa.builder()
                .nombre(dto.getNombre())
                .precioAdicional(dto.getPrecioAdicional() != null ? dto.getPrecioAdicional() : 0.0)
                .activo(true)
                .build();
    }

    public void updateEntity(Presa presa, PresaRequestDTO dto) {
        if (presa == null || dto == null) {
            return;
        }
        presa.setNombre(dto.getNombre());
        presa.setPrecioAdicional(dto.getPrecioAdicional() != null ? dto.getPrecioAdicional() : 0.0);
    }

    public PresaResponseDTO toResponseDTO(Presa presa) {
        if (presa == null) {
            return null;
        }
        return PresaResponseDTO.builder()
                .id(presa.getId())
                .nombre(presa.getNombre())
                .precioAdicional(presa.getPrecioAdicional())
                .activo(presa.getActivo())
                .build();
    }

    /**
     * Variante usada por los endpoints de menú: expone el stock del día sin
     * alterar los campos que antes no se serializaban (activo / porDefecto).
     */
    public PresaResponseDTO toResponseDTO(Presa presa, Integer stock) {
        if (presa == null) {
            return null;
        }
        return PresaResponseDTO.builder()
                .id(presa.getId())
                .nombre(presa.getNombre())
                .precioAdicional(presa.getPrecioAdicional())
                .stock(stock)
                .build();
    }

    /**
     * Variante usada dentro del menú cuando la presa está asociada a un plato.
     */
    public PresaResponseDTO toResponseDTO(Presa presa, Integer stock, Boolean porDefecto) {
        if (presa == null) {
            return null;
        }
        return PresaResponseDTO.builder()
                .id(presa.getId())
                .nombre(presa.getNombre())
                .precioAdicional(presa.getPrecioAdicional())
                .stock(stock)
                .porDefecto(porDefecto)
                .build();
    }

    public PresaResponseDTO toResponseDTO(PresaDia presaDia) {
        if (presaDia == null) {
            return null;
        }
        return toResponseDTO(presaDia.getPresa(), presaDia.getStock());
    }

    public List<PresaResponseDTO> toResponseDTOList(List<Presa> presas) {
        if (presas == null) {
            return List.of();
        }
        return presas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
