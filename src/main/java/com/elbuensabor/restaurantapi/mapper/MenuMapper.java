package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.response.PlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoMenu;
import com.elbuensabor.restaurantapi.model.PlatoMenuDia;
import com.elbuensabor.restaurantapi.model.PlatoMenuPresa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuMapper {

    private final PresaMapper presaMapper;

    public PlatoMenuResponseDTO toDTO(
            PlatoMenuDia platoDia,
            List<PlatoMenuPresa> presasDelPlato,
            Map<Long, Integer> stockPresas) {

        if (platoDia == null) {
            return null;
        }

        CatalogoPlatoMenu catalogo = platoDia.getCatalogoPlatoMenu();

        List<PresaResponseDTO> presasDTO = presasDelPlato == null
                ? List.of()
                : presasDelPlato.stream()
                        .filter(Objects::nonNull)
                        .map(pp -> {
                            Integer stock = pp.getPresa() != null && stockPresas != null
                                    ? stockPresas.getOrDefault(pp.getPresa().getId(), 0)
                                    : 0;
                            return presaMapper.toResponseDTO(pp.getPresa(), stock, pp.getPresaPorDefecto());
                        })
                        .collect(Collectors.toList());

        return PlatoMenuResponseDTO.builder()
                .id(catalogo != null ? catalogo.getId() : null)
                .nombre(catalogo != null ? catalogo.getNombre() : null)
                .tipoMenu(catalogo != null && catalogo.getTipoMenu() != null ? catalogo.getTipoMenu().name() : null)
                .stock(platoDia.getStock())
                .disponible(Boolean.TRUE.equals(platoDia.getDisponible())
                        && platoDia.getStock() != null
                        && platoDia.getStock() > 0)
                .permiteCambioPresa(catalogo != null ? catalogo.getPermiteCambioPresa() : null)
                .presasDisponibles(catalogo != null && Boolean.TRUE.equals(catalogo.getPermiteCambioPresa())
                        ? presasDTO
                        : null)
                .build();
    }
}
