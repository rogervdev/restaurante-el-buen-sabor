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
public class MenuDiaConfiguradoResponseDTO {
    private LocalDate fecha;
    private Integer totalPlatos;
    private Integer totalPresas;
    private List<PlatoMenuResponseDTO> platos;
    private List<PresaResponseDTO> presas;
}