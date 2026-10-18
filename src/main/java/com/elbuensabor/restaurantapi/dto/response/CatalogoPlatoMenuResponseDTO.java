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
public class CatalogoPlatoMenuResponseDTO {
    private Long id;
    private String nombre;
    private String tipoMenu;
    private Boolean permiteCambioPresa;
    private Boolean activo;
    private List<PresaResponseDTO> presasAsociadas;
}