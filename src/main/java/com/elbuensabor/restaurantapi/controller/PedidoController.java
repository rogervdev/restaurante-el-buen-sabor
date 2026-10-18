package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.request.PedidoRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PedidoResponseDTO;
import com.elbuensabor.restaurantapi.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        try {
            PedidoResponseDTO respuesta = pedidoService.crearPedido(request);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si falla el stock o algo, devolvemos un 400 Bad Request con el mensaje
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.cambiarEstadoPedido(id, estado));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<PedidoResponseDTO> anularPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.anularPedido(id));
    }
}