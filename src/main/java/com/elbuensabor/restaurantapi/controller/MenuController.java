package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.response.MenuDiaResponseDTO;
import com.elbuensabor.restaurantapi.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * GET /api/menu/hoy
     * Devuelve el menú completo del día actual
     */
    @GetMapping("/hoy")
    public ResponseEntity<MenuDiaResponseDTO> obtenerMenuDelDia() {
        MenuDiaResponseDTO menu = menuService.obtenerMenuDelDia();
        return ResponseEntity.ok(menu);
    }
}