package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.response.DashboardResumenResponseDTO;
import com.elbuensabor.restaurantapi.model.*;
import com.elbuensabor.restaurantapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final PlatoMenuDiaRepository platoMenuDiaRepository;
    private final PresaDiaRepository presaDiaRepository;

    public DashboardResumenResponseDTO obtenerResumen() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(LocalTime.MAX);

        // 1. Obtener pedidos del día
        List<Pedido> pedidosDelDia = pedidoRepository.findByFechaHoraBetween(inicioDia, finDia);

        // 2. Calcular ventas totales (excluyendo los anulados)
        Double ventasTotales = pedidosDelDia.stream()
                .filter(p -> !"ANULADO".equals(p.getEstado()))
                .mapToDouble(Pedido::getTotal)
                .sum();

        // 3. Contar pedidos por estado (ahora es String, no Enum)
        Map<String, Long> pedidosPorEstado = pedidosDelDia.stream()
                .collect(Collectors.groupingBy(Pedido::getEstado, Collectors.counting()));

        // 4. Obtener platos más vendidos
        List<DashboardResumenResponseDTO.PlatoMasVendidoDTO> platosMasVendidos = obtenerPlatosMasVendidos(pedidosDelDia);

        // 5. Obtener alertas de stock bajo
        List<DashboardResumenResponseDTO.AlertaStockDTO> alertasStock = obtenerAlertasStockBajo(hoy);

        return DashboardResumenResponseDTO.builder()
                .fecha(hoy)
                .ventasTotales(ventasTotales)
                .totalPedidos(pedidosDelDia.size())
                .pedidosPendientes(pedidosPorEstado.getOrDefault("PENDIENTE", 0L).intValue())
                .pedidosEnCocina(pedidosPorEstado.getOrDefault("EN_COCINA", 0L).intValue())
                .pedidosListos(pedidosPorEstado.getOrDefault("LISTO", 0L).intValue())
                .pedidosEntregados(pedidosPorEstado.getOrDefault("ENTREGADO", 0L).intValue())
                .platosMasVendidos(platosMasVendidos)
                .alertasStockBajo(alertasStock)
                .build();
    }

    private List<DashboardResumenResponseDTO.PlatoMasVendidoDTO> obtenerPlatosMasVendidos(List<Pedido> pedidos) {
        Map<String, List<PedidoDetalle>> platosVendidos = pedidos.stream()
                .filter(p -> !"ANULADO".equals(p.getEstado()))
                .flatMap(p -> p.getDetalles().stream())
                .collect(Collectors.groupingBy(d -> {
                    if (d.getCatalogoPlatoMenu() != null) {
                        return d.getCatalogoPlatoMenu().getNombre();
                    } else if (d.getCatalogoPlatoCarta() != null) {
                        return d.getCatalogoPlatoCarta().getNombre();
                    }
                    return "Desconocido";
                }));

        return platosVendidos.entrySet().stream()
                .map(entry -> {
                    String nombrePlato = entry.getKey();
                    List<PedidoDetalle> detalles = entry.getValue();
                    Integer cantidadVendida = detalles.stream().mapToInt(PedidoDetalle::getCantidad).sum();
                    Double ingresosGenerados = detalles.stream()
                            .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                            .sum();

                    return DashboardResumenResponseDTO.PlatoMasVendidoDTO.builder()
                            .nombrePlato(nombrePlato)
                            .cantidadVendida(cantidadVendida)
                            .ingresosGenerados(ingresosGenerados)
                            .build();
                })
                .sorted((a, b) -> b.getCantidadVendida().compareTo(a.getCantidadVendida()))
                .limit(10) // Top 10
                .collect(Collectors.toList());
    }

    private List<DashboardResumenResponseDTO.AlertaStockDTO> obtenerAlertasStockBajo(LocalDate hoy) {
        List<DashboardResumenResponseDTO.AlertaStockDTO> alertas = new ArrayList<>();

        // Alertas de platos del menú
        List<PlatoMenuDia> platosDelDia = platoMenuDiaRepository.findByFecha(hoy);
        platosDelDia.stream()
                .filter(p -> p.getStock() <= 5 && p.getStock() > 0)
                .forEach(p -> alertas.add(DashboardResumenResponseDTO.AlertaStockDTO.builder()
                        .nombreProducto(p.getCatalogoPlatoMenu().getNombre())
                        .tipo("PLATO")
                        .stockActual(p.getStock())
                        .build()));

        // Alertas de presas
        List<PresaDia> presasDelDia = presaDiaRepository.findByFecha(hoy);
        presasDelDia.stream()
                .filter(p -> p.getStock() <= 5 && p.getStock() > 0)
                .forEach(p -> alertas.add(DashboardResumenResponseDTO.AlertaStockDTO.builder()
                        .nombreProducto(p.getPresa().getNombre())
                        .tipo("PRESA")
                        .stockActual(p.getStock())
                        .build()));

        return alertas;
    }
}