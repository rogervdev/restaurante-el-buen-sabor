package com.elbuensabor.restaurantapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumenResponseDTO {
    private LocalDate fecha;
    private Double ventasTotales;
    private Integer totalPedidos;
    private Integer pedidosPendientes;
    private Integer pedidosEnCocina;
    private Integer pedidosListos;
    private Integer pedidosEntregados;
    private List<PlatoMasVendidoDTO> platosMasVendidos;
    private List<AlertaStockDTO> alertasStockBajo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatoMasVendidoDTO {
        private String nombrePlato;
        private Integer cantidadVendida;
        private Double ingresosGenerados;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertaStockDTO {
        private String nombreProducto;
        private String tipo; // PLATO o PRESA
        private Integer stockActual;
    }
}