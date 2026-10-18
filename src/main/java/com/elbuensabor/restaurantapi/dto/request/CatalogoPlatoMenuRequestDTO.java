package com.elbuensabor.restaurantapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoPlatoMenuRequestDTO {

    @NotBlank(message = "El nombre del plato es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo de menú es obligatorio")
    private String tipoMenu; // ENTRADA, SEGUNDO, BEBIDA

    private Boolean permiteCambioPresa = false;

    private List<Long> presaIds; // IDs de las presas asociadas (si permiteCambioPresa = true)
}