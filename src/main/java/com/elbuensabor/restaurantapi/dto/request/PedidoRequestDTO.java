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

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String clienteNombre;

    private String clienteTelefono;

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
    private Boolean requiereDelivery = false;
}