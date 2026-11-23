package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.response.PedidoResponseDTO;
import com.elbuensabor.restaurantapi.mapper.PedidoMapper;
import com.elbuensabor.restaurantapi.model.Pedido;
import com.elbuensabor.restaurantapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoConsultaService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public List<PedidoResponseDTO> obtenerPedidosDelDia() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(LocalTime.MAX);

        List<Pedido> pedidos = pedidoRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);

        return pedidoMapper.toResponseDTOList(pedidos);
    }
}
