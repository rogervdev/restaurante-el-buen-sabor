package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.request.PedidoDetalleRequestDTO;
import com.elbuensabor.restaurantapi.dto.request.PedidoRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PedidoDetalleResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PedidoResponseDTO;
import com.elbuensabor.restaurantapi.model.Cliente;
import com.elbuensabor.restaurantapi.model.Pedido;
import com.elbuensabor.restaurantapi.model.PedidoDetalle;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    /**
     * Construye el pedido con los datos de entrada. Los valores calculados
     * (total, montos, costos y detalles) se asignan luego en el servicio.
     */
    public Pedido toEntity(PedidoRequestDTO dto, Cliente cliente) {
        if (dto == null) {
            return null;
        }
        return Pedido.builder()
                .cliente(cliente)
                .clienteNombre(dto.getClienteNombre())
                .clienteTelefono(dto.getClienteTelefono())
                .clienteDocumento(dto.getClienteDocumento())
                .clienteDireccion(dto.getClienteDireccion())
                .tipoComprobante(dto.getTipoComprobante())
                .serieCorrelativo(dto.getSerieCorrelativo())
                .conceptoComprobante(dto.getConceptoComprobante())
                .tipo(dto.getTipo() != null ? dto.getTipo().toUpperCase() : null)
                .tipoVenta(dto.getTipoVenta() != null ? dto.getTipoVenta().toUpperCase() : null)
                .estado("PENDIENTE")
                .fechaHora(LocalDateTime.now())
                .requiereDelivery(dto.getRequiereDelivery() != null ? dto.getRequiereDelivery() : false)
                .build();
    }

    public PedidoDetalle toEntity(PedidoDetalleRequestDTO dto, Pedido pedido) {
        if (dto == null) {
            return null;
        }
        return PedidoDetalle.builder()
                .pedido(pedido)
                .cantidad(dto.getCantidad())
                .observacion(dto.getObservacion())
                .build();
    }

    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        boolean esMenu = "MENU".equals(pedido.getTipoVenta());
        List<PedidoDetalleResponseDTO> detalles = pedido.getDetalles() == null
                ? List.of()
                : pedido.getDetalles().stream()
                        .filter(Objects::nonNull)
                        .map(detalle -> toDetalleResponseDTO(detalle, esMenu))
                        .collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteNombre(pedido.getClienteNombre())
                .clienteTelefono(pedido.getClienteTelefono())
                .clienteDocumento(pedido.getClienteDocumento())
                .clienteDireccion(pedido.getClienteDireccion())
                .tipoComprobante(pedido.getTipoComprobante() != null ? pedido.getTipoComprobante() : "BOLETA")
                .serieCorrelativo(pedido.getSerieCorrelativo() != null ? pedido.getSerieCorrelativo()
                        : ("FACTURA".equals(pedido.getTipoComprobante()) ? "F001-0000000" : "B001-0000000"))
                .conceptoComprobante(pedido.getConceptoComprobante() != null ? pedido.getConceptoComprobante() : "DETALLADO")
                .tipo(pedido.getTipo())
                .tipoVenta(pedido.getTipoVenta())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .fechaHora(pedido.getFechaHora())
                .metodoPago(pedido.getMetodoPago())
                .montoRecibido(pedido.getMontoRecibido())
                .vuelto(pedido.getVuelto())
                .costoDescartable(pedido.getCostoDescartable())
                .requiereDelivery(pedido.getRequiereDelivery())
                .costoDelivery(pedido.getCostoDelivery())
                .detalles(detalles)
                .build();
    }

    public PedidoDetalleResponseDTO toDetalleResponseDTO(PedidoDetalle detalle, boolean esMenu) {
        if (detalle == null) {
            return null;
        }

        PedidoDetalleResponseDTO.PedidoDetalleResponseDTOBuilder builder = PedidoDetalleResponseDTO.builder()
                .id(detalle.getId())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .observacion(detalle.getObservacion());

        if (esMenu) {
            builder.nombreSegundo(detalle.getCatalogoPlatoMenu() != null ? detalle.getCatalogoPlatoMenu().getNombre() : null);
            builder.nombreEntrada(detalle.getEntrada() != null ? detalle.getEntrada().getNombre() : null);
            builder.nombreBebida(detalle.getBebida() != null ? detalle.getBebida().getNombre() : null);
            builder.nombrePresa(detalle.getPresa() != null ? detalle.getPresa().getNombre() : null);
        } else {
            builder.nombrePlatoCarta(detalle.getCatalogoPlatoCarta() != null ? detalle.getCatalogoPlatoCarta().getNombre() : null);
        }

        return builder.build();
    }

    public List<PedidoResponseDTO> toResponseDTOList(List<Pedido> pedidos) {
        if (pedidos == null) {
            return List.of();
        }
        return pedidos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
