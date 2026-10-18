package com.elbuensabor.restaurantapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoDetalleResponseDTO {
    private Long id;
    private String nombreSegundo;   // El segundo (para MENÚ)
    private String nombreEntrada;   // La entrada (opcional)
    private String nombreBebida;    // La bebida (opcional)
    private String nombrePresa;     // Cambio de presa (opcional)
    private String nombrePlatoCarta; // Para CARTA
    private Integer cantidad;
    private Double precioUnitario;
    private String observacion;
}