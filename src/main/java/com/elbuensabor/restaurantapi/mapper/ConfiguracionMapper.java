package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.response.ConfiguracionResponseDTO;
import com.elbuensabor.restaurantapi.model.Configuracion;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionMapper {

    public ConfiguracionResponseDTO toResponseDTO(Configuracion configuracion) {
        if (configuracion == null) {
            return null;
        }
        return ConfiguracionResponseDTO.builder()
                .id(configuracion.getId())
                .precioMenu(configuracion.getPrecioMenu())
                .precioMenuSinEntrada(configuracion.getPrecioMenuSinEntrada())
                .costoDescartable(configuracion.getCostoDescartable())
                .costoDelivery(configuracion.getCostoDelivery())
                .fechaActualizacion(configuracion.getFechaActualizacion())
                .build();
    }
}
