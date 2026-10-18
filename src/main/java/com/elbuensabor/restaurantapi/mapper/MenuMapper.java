package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.response.PlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoMenu;
import com.elbuensabor.restaurantapi.model.PlatoMenuDia;
import com.elbuensabor.restaurantapi.model.PlatoMenuPresa;
import com.elbuensabor.restaurantapi.model.PresaDia;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuMapper {

    public PlatoMenuResponseDTO toDTO(
            PlatoMenuDia platoDia,
            List<PlatoMenuPresa> presasDelPlato,
            Map<Long, Integer> stockPresas) {

        CatalogoPlatoMenu catalogo = platoDia.getCatalogoPlatoMenu();

        // ✅ CORRECCIÓN: Eliminamos el .filter() para que el frontend reciba TODAS las presas
        // y pueda mostrar las que tienen stock 0 como "Agotado".
        List<PresaResponseDTO> presasDTO = presasDelPlato.stream()
                .map(pp -> {
                    Integer stock = stockPresas.getOrDefault(pp.getPresa().getId(), 0);
                    return PresaResponseDTO.builder()
                            .id(pp.getPresa().getId())
                            .nombre(pp.getPresa().getNombre())
                            .precioAdicional(pp.getPresa().getPrecioAdicional())
                            .stock(stock)
                            .porDefecto(pp.getPresaPorDefecto())
                            .build();
                })
                .collect(Collectors.toList());

        return PlatoMenuResponseDTO.builder()
                .id(catalogo.getId())
                .nombre(catalogo.getNombre())
                .tipoMenu(catalogo.getTipoMenu().name())
                .stock(platoDia.getStock())
                .disponible(platoDia.getDisponible() && platoDia.getStock() > 0)
                .permiteCambioPresa(catalogo.getPermiteCambioPresa())
                .presasDisponibles(catalogo.getPermiteCambioPresa() ? presasDTO : null)
                .build();
    }
}