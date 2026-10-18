package com.elbuensabor.restaurantapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDiaResponseDTO {
    private LocalDate fecha;
    private Double precioMenu;
    private List<PlatoMenuResponseDTO> entradas;
    private List<PlatoMenuResponseDTO> segundos;
    private List<PlatoMenuResponseDTO> bebidas;
}