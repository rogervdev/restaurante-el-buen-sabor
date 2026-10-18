package com.elbuensabor.restaurantapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoResponseDTO {
    private Long id;
    private String clienteNombre;
    private String clienteTelefono;
    private String tipo;
    private String tipoVenta;
    private String estado;
    private Double total;
    private String metodoPago;
    private Double montoRecibido;
    private Double vuelto;

    // ✅ NUEVOS CAMPOS
    private Double costoDescartable;
    private Boolean requiereDelivery;
    private Double costoDelivery;

    private LocalDateTime fechaHora;
    private List<PedidoDetalleResponseDTO> detalles;
}