package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.ConfiguracionRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.ConfiguracionResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import com.elbuensabor.restaurantapi.mapper.ConfiguracionMapper;
import com.elbuensabor.restaurantapi.model.Configuracion;
import com.elbuensabor.restaurantapi.repository.ConfiguracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final ConfiguracionMapper configuracionMapper;

    public ConfiguracionResponseDTO obtenerConfiguracion() {
        Configuracion config = configuracionRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No hay configuración en la base de datos. Ejecuta el INSERT en MySQL."));

        return configuracionMapper.toResponseDTO(config);
    }

    @Transactional
    public ConfiguracionResponseDTO actualizar(ConfiguracionRequestDTO request) {
        Configuracion config = configuracionRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> Configuracion.builder().build());

        config.setPrecioMenu(request.getPrecioMenu());
        config.setPrecioMenuSinEntrada(request.getPrecioMenuSinEntrada());
        config.setCostoDescartable(request.getCostoDescartable());
        config.setCostoDelivery(request.getCostoDelivery());
        config.setFechaActualizacion(LocalDateTime.now());

        Configuracion configActualizada = configuracionRepository.save(config);
        return configuracionMapper.toResponseDTO(configActualizada);
    }
}
