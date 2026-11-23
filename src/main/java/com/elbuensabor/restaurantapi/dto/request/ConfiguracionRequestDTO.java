package com.elbuensabor.restaurantapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionRequestDTO {

    @NotNull(message = "El precio del menú completo es obligatorio")
    @PositiveOrZero(message = "El precio del menú completo no puede ser negativo")
    private Double precioMenu;

    @NotNull(message = "El precio del menú sin entrada es obligatorio")
    @PositiveOrZero(message = "El precio del menú sin entrada no puede ser negativo")
    private Double precioMenuSinEntrada;

    @NotNull(message = "El costo de descartable es obligatorio")
    @PositiveOrZero(message = "El costo de descartable no puede ser negativo")
    private Double costoDescartable;

    @NotNull(message = "El costo de delivery es obligatorio")
    @PositiveOrZero(message = "El costo de delivery no puede ser negativo")
    private Double costoDelivery;
}
