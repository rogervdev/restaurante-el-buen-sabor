package com.elbuensabor.restaurantapi.controller;


import com.elbuensabor.restaurantapi.model.Configuracion;
import com.elbuensabor.restaurantapi.repository.ConfiguracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // ✅ La ruta base
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionRepository configuracionRepository;

    // ✅ Esta es la ruta exacta que pide tu frontend
    @GetMapping("/configuracion")
    public ResponseEntity<Configuracion> obtenerConfiguracion() {
        // Busca la primera (y única) fila de la tabla configuración
        Configuracion config = configuracionRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay configuración en la base de datos. Ejecuta el INSERT en MySQL."));

        return ResponseEntity.ok(config);
    }
}