package com.elbuensabor.restaurantapi.controller;

import com.elbuensabor.restaurantapi.dto.response.RucResponseDTO;
import com.elbuensabor.restaurantapi.service.ConsultaRucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaRucService consultaRucService;

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<RucResponseDTO> consultarRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(consultaRucService.consultarRuc(ruc));
    }
}
