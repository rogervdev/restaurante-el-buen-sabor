package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoCartaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoCartaResponseDTO;
import com.elbuensabor.restaurantapi.service.CatalogoPlatoCartaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos-carta")
@RequiredArgsConstructor
public class CatalogoPlatoCartaController {

    private final CatalogoPlatoCartaService catalogoPlatoCartaService;

    @GetMapping
    public ResponseEntity<List<CatalogoPlatoCartaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(catalogoPlatoCartaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoPlatoCartaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoPlatoCartaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CatalogoPlatoCartaResponseDTO> crear(@Valid @RequestBody CatalogoPlatoCartaRequestDTO request) {
        return new ResponseEntity<>(catalogoPlatoCartaService.crear(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoPlatoCartaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CatalogoPlatoCartaRequestDTO request) {
        return ResponseEntity.ok(catalogoPlatoCartaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoPlatoCartaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}