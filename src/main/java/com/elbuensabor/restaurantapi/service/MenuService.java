package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.response.MenuDiaResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.mapper.MenuMapper;
import com.elbuensabor.restaurantapi.model.Configuracion;
import com.elbuensabor.restaurantapi.model.PlatoMenuDia;
import com.elbuensabor.restaurantapi.model.PlatoMenuPresa;
import com.elbuensabor.restaurantapi.model.PresaDia;
import com.elbuensabor.restaurantapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final PlatoMenuDiaRepository platoMenuDiaRepository;
    private final PresaDiaRepository presaDiaRepository;
    private final ConfiguracionRepository configuracionRepository;
    private final MenuMapper menuMapper;

    public MenuDiaResponseDTO obtenerMenuDelDia() {
        LocalDate hoy = LocalDate.now();

        // 1. Obtener precio del menú
        Configuracion config = configuracionRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay configuración de precio del menú"));

        // 🚀 2. OPTIMIZACIÓN: 1 sola consulta SQL con JOIN FETCH (trae platos + presas de una vez)
        List<PlatoMenuDia> platosDelDia = platoMenuDiaRepository.buscarConRelaciones(hoy);

        // 3. Obtener stock de presas del día
        List<PresaDia> presasDelDia = presaDiaRepository.findByFecha(hoy);
        Map<Long, Integer> stockPresas = presasDelDia.stream()
                .collect(Collectors.toMap(pd -> pd.getPresa().getId(), PresaDia::getStock));

        // 4. Convertir a DTOs (Ya NO consultamos la base de datos dentro del map)
        List<PlatoMenuResponseDTO> platosDTO = platosDelDia.stream()
                .map(platoDia -> {
                    // Las presas ya vienen cargadas en la entidad gracias al JOIN FETCH
                    List<PlatoMenuPresa> presasAsociadas = platoDia.getCatalogoPlatoMenu().getPlatoMenuPresas();
                    return menuMapper.toDTO(platoDia, presasAsociadas, stockPresas);
                })
                .collect(Collectors.toList());

        // 5. Separar por tipo
        List<PlatoMenuResponseDTO> entradas = platosDTO.stream().filter(p -> "ENTRADA".equals(p.getTipoMenu())).collect(Collectors.toList());
        List<PlatoMenuResponseDTO> segundos = platosDTO.stream().filter(p -> "SEGUNDO".equals(p.getTipoMenu())).collect(Collectors.toList());
        List<PlatoMenuResponseDTO> bebidas = platosDTO.stream().filter(p -> "BEBIDA".equals(p.getTipoMenu())).collect(Collectors.toList());

        // 6. Construir el DTO final
        return MenuDiaResponseDTO.builder()
                .fecha(hoy)
                .precioMenu(config.getPrecioMenu())
                .entradas(entradas)
                .segundos(segundos)
                .bebidas(bebidas)
                .build();
    }
}