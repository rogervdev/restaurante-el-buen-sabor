package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.ConfigurarMenuDiaRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.MenuDiaConfiguradoResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.mapper.MenuMapper;
import com.elbuensabor.restaurantapi.model.*;
import com.elbuensabor.restaurantapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuDiaService {

    private final PlatoMenuDiaRepository platoMenuDiaRepository;
    private final PresaDiaRepository presaDiaRepository;
    private final CatalogoPlatoMenuRepository catalogoPlatoMenuRepository;
    private final PresaRepository presaRepository;
    private final PlatoMenuPresaRepository platoMenuPresaRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public MenuDiaConfiguradoResponseDTO configurarMenuDia(ConfigurarMenuDiaRequestDTO request) {
        LocalDate hoy = LocalDate.now();

        // 1. Eliminar configuración anterior del día (si existe)
        platoMenuDiaRepository.deleteByFecha(hoy);
        presaDiaRepository.deleteByFecha(hoy);

        // 2. Guardar nuevos platos del día
        List<PlatoMenuResponseDTO> platosDTO = new ArrayList<>();
        for (ConfigurarMenuDiaRequestDTO.PlatoDiaItemDTO item : request.getPlatos()) {
            CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(item.getCatalogoPlatoMenuId())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + item.getCatalogoPlatoMenuId()));

            PlatoMenuDia platoDia = new PlatoMenuDia();
            platoDia.setCatalogoPlatoMenu(plato);
            platoDia.setFecha(hoy);
            platoDia.setStock(item.getStock());
            platoDia.setDisponible(item.getStock() > 0);
            platoMenuDiaRepository.save(platoDia);

            // Obtener presas asociadas a este plato
            List<PlatoMenuPresa> presasAsociadas = platoMenuPresaRepository.findByCatalogoPlatoMenuId(plato.getId());

            // Obtener stock de presas del día
            Map<Long, Integer> stockPresas = request.getPresas().stream()
                    .collect(Collectors.toMap(
                            ConfigurarMenuDiaRequestDTO.PresaDiaItemDTO::getPresaId,
                            ConfigurarMenuDiaRequestDTO.PresaDiaItemDTO::getStock
                    ));

            platosDTO.add(menuMapper.toDTO(platoDia, presasAsociadas, stockPresas));
        }

        // 3. Guardar stock de presas del día
        List<PresaResponseDTO> presasDTO = new ArrayList<>();
        for (ConfigurarMenuDiaRequestDTO.PresaDiaItemDTO item : request.getPresas()) {
            Presa presa = presaRepository.findById(item.getPresaId())
                    .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + item.getPresaId()));

            PresaDia presaDia = new PresaDia();
            presaDia.setPresa(presa);
            presaDia.setFecha(hoy);
            presaDia.setStock(item.getStock());
            presaDiaRepository.save(presaDia);

            presasDTO.add(PresaResponseDTO.builder()
                    .id(presa.getId())
                    .nombre(presa.getNombre())
                    .precioAdicional(presa.getPrecioAdicional())
                    .stock(item.getStock())
                    .build());
        }

        // 4. Retornar respuesta
        return MenuDiaConfiguradoResponseDTO.builder()
                .fecha(hoy)
                .totalPlatos(platosDTO.size())
                .totalPresas(presasDTO.size())
                .platos(platosDTO)
                .presas(presasDTO)
                .build();
    }

    public MenuDiaConfiguradoResponseDTO obtenerMenuDiaConfigurado() {
        LocalDate hoy = LocalDate.now();

        // 🚀 OPTIMIZACIÓN: Usar buscarConRelaciones para evitar N+1
        List<PlatoMenuDia> platosDelDia = platoMenuDiaRepository.buscarConRelaciones(hoy);
        List<PresaDia> presasDelDia = presaDiaRepository.findByFecha(hoy);

        Map<Long, Integer> stockPresas = presasDelDia.stream()
                .collect(Collectors.toMap(pd -> pd.getPresa().getId(), PresaDia::getStock));

        List<PlatoMenuResponseDTO> platosDTO = platosDelDia.stream()
                .map(platoDia -> {
                    // Las presas ya vienen cargadas, no necesitamos consultar el repository de nuevo
                    List<PlatoMenuPresa> presasAsociadas = platoDia.getCatalogoPlatoMenu().getPlatoMenuPresas();
                    return menuMapper.toDTO(platoDia, presasAsociadas, stockPresas);
                })
                .collect(Collectors.toList());

        List<PresaResponseDTO> presasDTO = presasDelDia.stream()
                .map(pd -> PresaResponseDTO.builder()
                        .id(pd.getPresa().getId())
                        .nombre(pd.getPresa().getNombre())
                        .precioAdicional(pd.getPresa().getPrecioAdicional())
                        .stock(pd.getStock())
                        .build())
                .collect(Collectors.toList());

        return MenuDiaConfiguradoResponseDTO.builder()
                .fecha(hoy)
                .totalPlatos(platosDTO.size())
                .totalPresas(presasDTO.size())
                .platos(platosDTO)
                .presas(presasDTO)
                .build();
    }
}