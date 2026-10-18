package com.elbuensabor.restaurantapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PresaResponseDTO {
    private Long id;
    private String nombre;
    private Double precioAdicional;
    private Boolean activo;
    // ✅ AGREGAR ESTOS DOS CAMPOS QUE FALTABAN:
    private Integer stock;
    private Boolean porDefecto;
}