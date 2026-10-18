package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.request.PresaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.service.PresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presas")
@RequiredArgsConstructor
public class PresaController {

    private final PresaService presaService;

    @GetMapping
    public ResponseEntity<List<PresaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(presaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(presaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PresaResponseDTO> crear(@Valid @RequestBody PresaRequestDTO request) {
        return new ResponseEntity<>(presaService.crear(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PresaRequestDTO request) {
        return ResponseEntity.ok(presaService.actualizar(id, request));
    }

    @PatchMapping("/{id}/deshabilitar")
    public ResponseEntity<PresaResponseDTO> deshabilitar(@PathVariable Long id) {
        return ResponseEntity.ok(presaService.deshabilitar(id));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<PresaResponseDTO> activar(@PathVariable Long id) {
        return ResponseEntity.ok(presaService.activar(id));
    }
}