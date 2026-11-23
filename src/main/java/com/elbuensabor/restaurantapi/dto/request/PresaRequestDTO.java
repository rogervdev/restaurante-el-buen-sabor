package com.elbuensabor.restaurantapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresaRequestDTO {

    @NotBlank(message = "El nombre de la presa es obligatorio")
    private String nombre;

    private Double precioAdicional;
}