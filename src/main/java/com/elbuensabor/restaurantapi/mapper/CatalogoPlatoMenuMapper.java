package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoMenuRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoMenu;
import com.elbuensabor.restaurantapi.model.PlatoMenuPresa;
import com.elbuensabor.restaurantapi.repository.PlatoMenuPresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CatalogoPlatoMenuMapper {

    private final PlatoMenuPresaRepository platoMenuPresaRepository;
    private final PresaMapper presaMapper;

    public CatalogoPlatoMenu toEntity(CatalogoPlatoMenuRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return CatalogoPlatoMenu.builder()
                .nombre(dto.getNombre())
                .tipoMenu(parseTipoMenu(dto.getTipoMenu()))
                .permiteCambioPresa(Boolean.TRUE.equals(dto.getPermiteCambioPresa()))
                .activo(true)
                .build();
    }

    public void updateEntity(CatalogoPlatoMenu plato, CatalogoPlatoMenuRequestDTO dto) {
        if (plato == null || dto == null) {
            return;
        }
        plato.setNombre(dto.getNombre());
        plato.setTipoMenu(parseTipoMenu(dto.getTipoMenu()));
        plato.setPermiteCambioPresa(Boolean.TRUE.equals(dto.getPermiteCambioPresa()));
    }

    public CatalogoPlatoMenuResponseDTO toResponseDTO(CatalogoPlatoMenu plato) {
        if (plato == null) {
            return null;
        }
        List<PlatoMenuPresa> presasAsociadas = plato.getId() != null
                ? platoMenuPresaRepository.findByCatalogoPlatoMenuId(plato.getId())
                : List.of();
        return toResponseDTO(plato, presasAsociadas);
    }

    public CatalogoPlatoMenuResponseDTO toResponseDTO(CatalogoPlatoMenu plato,
                                                      List<PlatoMenuPresa> presasAsociadas) {
        if (plato == null) {
            return null;
        }

        List<PresaResponseDTO> presasDTO = presasAsociadas == null
                ? List.of()
                : presasAsociadas.stream()
                        .filter(Objects::nonNull)
                        .map(pmp -> presaMapper.toResponseDTO(
                                pmp.getPresa(),
                                null,
                                pmp.getPresaPorDefecto()))
                        .collect(Collectors.toList());

        return CatalogoPlatoMenuResponseDTO.builder()
                .id(plato.getId())
                .nombre(plato.getNombre())
                .tipoMenu(plato.getTipoMenu() != null ? plato.getTipoMenu().name() : null)
                .permiteCambioPresa(plato.getPermiteCambioPresa())
                .activo(plato.getActivo())
                .presasAsociadas(Boolean.TRUE.equals(plato.getPermiteCambioPresa()) ? presasDTO : null)
                .build();
    }

    public List<CatalogoPlatoMenuResponseDTO> toResponseDTOList(List<CatalogoPlatoMenu> platos) {
        if (platos == null) {
            return List.of();
        }
        return platos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private CatalogoPlatoMenu.TipoMenu parseTipoMenu(String tipoMenu) {
        return tipoMenu != null ? CatalogoPlatoMenu.TipoMenu.valueOf(tipoMenu) : null;
    }
}
