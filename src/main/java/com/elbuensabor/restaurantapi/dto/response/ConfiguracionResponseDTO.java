package com.elbuensabor.restaurantapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionResponseDTO {
    private Long id;
    private Double precioMenu;
    private Double precioMenuSinEntrada;
    private Double costoDescartable;
    private Double costoDelivery;
    private LocalDateTime fechaActualizacion;
}
