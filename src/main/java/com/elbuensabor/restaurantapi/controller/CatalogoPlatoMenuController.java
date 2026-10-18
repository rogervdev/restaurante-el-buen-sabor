package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoMenuRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.service.CatalogoPlatoMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos-menu")
@RequiredArgsConstructor
public class CatalogoPlatoMenuController {

    private final CatalogoPlatoMenuService catalogoPlatoMenuService;

    @GetMapping
    public ResponseEntity<List<CatalogoPlatoMenuResponseDTO>> listarTodos() {
        return ResponseEntity.ok(catalogoPlatoMenuService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoPlatoMenuResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoPlatoMenuService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CatalogoPlatoMenuResponseDTO> crear(@Valid @RequestBody CatalogoPlatoMenuRequestDTO request) {
        return new ResponseEntity<>(catalogoPlatoMenuService.crear(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoPlatoMenuResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CatalogoPlatoMenuRequestDTO request) {
        return ResponseEntity.ok(catalogoPlatoMenuService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoPlatoMenuService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CatalogoPlatoMenuResponseDTO> cambiarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoPlatoMenuService.cambiarEstado(id));
    }
}