package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.response.PedidoResponseDTO;
import com.elbuensabor.restaurantapi.service.PedidoConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoConsultaController {

    private final PedidoConsultaService pedidoConsultaService;

    @GetMapping("/hoy")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosDelDia() {
        return ResponseEntity.ok(pedidoConsultaService.obtenerPedidosDelDia());
    }
}