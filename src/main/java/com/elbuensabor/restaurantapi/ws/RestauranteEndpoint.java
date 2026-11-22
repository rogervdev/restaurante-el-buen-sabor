package com.elbuensabor.restaurantapi.ws;

import com.elbuensabor.restaurantapi.model.Pedido;
import com.elbuensabor.restaurantapi.model.PlatoMenuDia;
import com.elbuensabor.restaurantapi.repository.PedidoRepository;
import com.elbuensabor.restaurantapi.repository.PlatoMenuDiaRepository;
import com.elbuensabor.restaurantapi.ws.dto.ConsultarMenuDelDiaRequest;
import com.elbuensabor.restaurantapi.ws.dto.ConsultarMenuDelDiaResponse;
import com.elbuensabor.restaurantapi.ws.dto.ConsultarPedidoRequest;
import com.elbuensabor.restaurantapi.ws.dto.ConsultarPedidoResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Endpoint
public class RestauranteEndpoint {

    private static final String NAMESPACE_URI = "http://elbuensabor.com/schema";

    private final PlatoMenuDiaRepository platoMenuDiaRepository;
    private final PedidoRepository pedidoRepository;

    public RestauranteEndpoint(PlatoMenuDiaRepository platoMenuDiaRepository,
                               PedidoRepository pedidoRepository) {
        this.platoMenuDiaRepository = platoMenuDiaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // Operación 1: Consultar Menú del Día
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarMenuDelDiaRequest")
    @ResponsePayload
    public ConsultarMenuDelDiaResponse consultarMenuDelDia(@RequestPayload ConsultarMenuDelDiaRequest request) {
        List<PlatoMenuDia> menuHoy = platoMenuDiaRepository.findByFecha(LocalDate.now());

        return ConsultarMenuDelDiaResponse.builder()
                .fecha(LocalDate.now().toString())
                .totalPlatos(menuHoy != null ? menuHoy.size() : 0)
                .mensaje("Consulta exitosa vía SOAP. Platos disponibles para hoy: " + (menuHoy != null ? menuHoy.size() : 0))
                .build();
    }

    // Operación 2: Consultar Pedido por ID
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarPedidoRequest")
    @ResponsePayload
    public ConsultarPedidoResponse consultarPedido(@RequestPayload ConsultarPedidoRequest request) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(request.getPedidoId());

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            return ConsultarPedidoResponse.builder()
                    .pedidoId(pedido.getId())
                    .cliente(pedido.getClienteNombre())
                    .total(pedido.getTotal() != null ? pedido.getTotal().doubleValue() : 0.0)
                    .estado(pedido.getEstado())
                    .mensaje("Pedido encontrado exitosamente vía SOAP.")
                    .build();
        }

        return ConsultarPedidoResponse.builder()
                .pedidoId(request.getPedidoId())
                .mensaje("Error: El pedido con ID " + request.getPedidoId() + " no existe.")
                .build();
    }
}