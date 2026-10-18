package com.elbuensabor.restaurantapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatoMenuResponseDTO {
    private Long id;
    private String nombre;
    private String tipoMenu; // ENTRADA, SEGUNDO, BEBIDA
    private Integer stock;
    private Boolean disponible;
    private Boolean permiteCambioPresa;
    private List<PresaResponseDTO> presasDisponibles; // Solo si permiteCambioPresa = true
}