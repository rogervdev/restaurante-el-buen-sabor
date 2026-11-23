package com.elbuensabor.restaurantapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    private Long clienteId;

    private String clienteNombre;

    private String clienteTelefono;

    private String clienteNotas;

    @Builder.Default
    private String tipoComprobante = "BOLETA";

    private String serieCorrelativo;

    @Builder.Default
    private String conceptoComprobante = "DETALLADO";

    private String clienteDocumento;

    private String clienteDireccion;

    @NotBlank(message = "El tipo de pedido es obligatorio")
    private String tipo; // MESA, LLEVAR

    @NotBlank(message = "El tipo de venta es obligatorio")
    private String tipoVenta; // MENU, CARTA

    @NotNull(message = "Los detalles del pedido son obligatorios")
    private List<PedidoDetalleRequestDTO> detalles;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago; // EFECTIVO, YAPE, PLIN, TARJETA, TRANSFERENCIA

    private Double montoRecibido;

    // ✅ NUEVO: ¿Requiere delivery a domicilio?
    @Builder.Default
    private Boolean requiereDelivery = false;
}