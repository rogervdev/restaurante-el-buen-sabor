package com.elbuensabor.restaurantapi.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalleRequestDTO {

    private Long platoId;                  // Para MENÚ (puede ser null en CARTA)
    private Long catalogoPlatoCartaId;     // Para CARTA (puede ser null en MENÚ)
    private Long presaId;                  // Cambio de presa (opcional)
    private Long entradaId;                // Entrada del menú (opcional, solo MENÚ)
    private Long bebidaId;                 // Bebida del menú (opcional)

    private Integer cantidad;

    // ✅ AGREGADO: Para que el deserializador de Jackson no rechace la petición
    private Double precioUnitario;

    private String observacion;
}