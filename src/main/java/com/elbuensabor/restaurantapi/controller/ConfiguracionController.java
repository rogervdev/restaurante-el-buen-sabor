package com.elbuensabor.restaurantapi.controller;


import com.elbuensabor.restaurantapi.dto.request.ConfiguracionRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.ConfiguracionResponseDTO;
import com.elbuensabor.restaurantapi.service.ConfiguracionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // ✅ La ruta base
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    // ✅ Esta es la ruta exacta que pide tu frontend
    @GetMapping("/configuracion")
    public ResponseEntity<ConfiguracionResponseDTO> obtenerConfiguracion() {
        return ResponseEntity.ok(configuracionService.obtenerConfiguracion());
    }

    @PutMapping("/configuracion")
    public ResponseEntity<ConfiguracionResponseDTO> actualizarConfiguracion(
            @Valid @RequestBody ConfiguracionRequestDTO request) {
        return ResponseEntity.ok(configuracionService.actualizar(request));
    }
}
