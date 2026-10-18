package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.response.PedidoDetalleResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PedidoResponseDTO;
import com.elbuensabor.restaurantapi.model.Pedido;
import com.elbuensabor.restaurantapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoConsultaService {

    private final PedidoRepository pedidoRepository;

    public List<PedidoResponseDTO> obtenerPedidosDelDia() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(LocalTime.MAX);

        List<Pedido> pedidos = pedidoRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);

        return pedidos.stream().map(this::convertirAResponseDTO).collect(Collectors.toList());
    }

    private PedidoResponseDTO convertirAResponseDTO(Pedido pedido) {
        // ✅ Mapeo actualizado con los nuevos campos del DTO
        List<PedidoDetalleResponseDTO> detalles = pedido.getDetalles().stream()
                .map(detalle -> {
                    PedidoDetalleResponseDTO.PedidoDetalleResponseDTOBuilder builder = PedidoDetalleResponseDTO.builder()
                            .id(detalle.getId())
                            .cantidad(detalle.getCantidad())
                            .precioUnitario(detalle.getPrecioUnitario())
                            .observacion(detalle.getObservacion());

                    if ("MENU".equals(pedido.getTipoVenta())) {
                        builder.nombreSegundo(detalle.getCatalogoPlatoMenu() != null ? detalle.getCatalogoPlatoMenu().getNombre() : null);
                        builder.nombreEntrada(detalle.getEntrada() != null ? detalle.getEntrada().getNombre() : null);
                        builder.nombreBebida(detalle.getBebida() != null ? detalle.getBebida().getNombre() : null);
                        builder.nombrePresa(detalle.getPresa() != null ? detalle.getPresa().getNombre() : null);
                    } else {
                        builder.nombrePlatoCarta(detalle.getCatalogoPlatoCarta() != null ? detalle.getCatalogoPlatoCarta().getNombre() : null);
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteNombre(pedido.getClienteNombre())
                .clienteTelefono(pedido.getClienteTelefono())
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
}