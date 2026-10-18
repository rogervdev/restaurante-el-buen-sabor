package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.request.ConfigurarMenuDiaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.MenuDiaConfiguradoResponseDTO;
import com.elbuensabor.restaurantapi.service.MenuDiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-dia")
@RequiredArgsConstructor
public class MenuDiaController {

    private final MenuDiaService menuDiaService;

    @PostMapping("/configurar")
    public ResponseEntity<MenuDiaConfiguradoResponseDTO> configurarMenuDia(
            @Valid @RequestBody ConfigurarMenuDiaRequestDTO request) {
        return new ResponseEntity<>(menuDiaService.configurarMenuDia(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<MenuDiaConfiguradoResponseDTO> obtenerMenuDiaConfigurado() {
        return ResponseEntity.ok(menuDiaService.obtenerMenuDiaConfigurado());
    }
}