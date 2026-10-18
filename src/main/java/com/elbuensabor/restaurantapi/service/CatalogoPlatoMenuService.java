package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoMenuRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PresaResponseDTO;
import com.elbuensabor.restaurantapi.model.CatalogoPlatoMenu;
import com.elbuensabor.restaurantapi.model.PlatoMenuPresa;
import com.elbuensabor.restaurantapi.model.Presa;
import com.elbuensabor.restaurantapi.repository.CatalogoPlatoMenuRepository;
import com.elbuensabor.restaurantapi.repository.PlatoMenuPresaRepository;
import com.elbuensabor.restaurantapi.repository.PresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoPlatoMenuService {

    private final CatalogoPlatoMenuRepository catalogoPlatoMenuRepository;
    private final PlatoMenuPresaRepository platoMenuPresaRepository;
    private final PresaRepository presaRepository;

    public List<CatalogoPlatoMenuResponseDTO> listarTodos() {
        return catalogoPlatoMenuRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public CatalogoPlatoMenuResponseDTO obtenerPorId(Long id) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato del menú no encontrado con ID: " + id));
        return convertirAResponseDTO(plato);
    }

    @Transactional
    public CatalogoPlatoMenuResponseDTO crear(CatalogoPlatoMenuRequestDTO request) {
        CatalogoPlatoMenu plato = new CatalogoPlatoMenu();
        plato.setNombre(request.getNombre());
        plato.setTipoMenu(CatalogoPlatoMenu.TipoMenu.valueOf(request.getTipoMenu()));
        plato.setPermiteCambioPresa(request.getPermiteCambioPresa());
        plato.setActivo(true);

        CatalogoPlatoMenu platoGuardado = catalogoPlatoMenuRepository.save(plato);

        // Si permite cambio de presa, asociar las presas
        if (request.getPermiteCambioPresa() && request.getPresaIds() != null) {
            asociarPresas(platoGuardado, request.getPresaIds());
        }

        return convertirAResponseDTO(platoGuardado);
    }


    @Transactional
    public CatalogoPlatoMenuResponseDTO cambiarEstado(Long id) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato del menú no encontrado con ID: " + id));

        plato.setActivo(!plato.getActivo()); // Invierte el estado
        return convertirAResponseDTO(catalogoPlatoMenuRepository.save(plato));
    }

    @Transactional
    public CatalogoPlatoMenuResponseDTO actualizar(Long id, CatalogoPlatoMenuRequestDTO request) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato del menú no encontrado con ID: " + id));

        plato.setNombre(request.getNombre());
        plato.setTipoMenu(CatalogoPlatoMenu.TipoMenu.valueOf(request.getTipoMenu()));
        plato.setPermiteCambioPresa(request.getPermiteCambioPresa());

        // Eliminar asociaciones antiguas de presas
        platoMenuPresaRepository.deleteByCatalogoPlatoMenuId(id);

        // Si permite cambio de presa, asociar las nuevas presas
        if (request.getPermiteCambioPresa() && request.getPresaIds() != null) {
            asociarPresas(plato, request.getPresaIds());
        }

        CatalogoPlatoMenu platoActualizado = catalogoPlatoMenuRepository.save(plato);
        return convertirAResponseDTO(platoActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!catalogoPlatoMenuRepository.existsById(id)) {
            throw new RuntimeException("Plato del menú no encontrado con ID: " + id);
        }
        catalogoPlatoMenuRepository.deleteById(id);
    }

    private void asociarPresas(CatalogoPlatoMenu plato, List<Long> presaIds) {
        for (int i = 0; i < presaIds.size(); i++) {
            Long presaId = presaIds.get(i);
            Presa presa = presaRepository.findById(presaId)
                    .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + presaId));

            PlatoMenuPresa asociacion = new PlatoMenuPresa();
            asociacion.setCatalogoPlatoMenu(plato);
            asociacion.setPresa(presa);
            asociacion.setPresaPorDefecto(i == 0); // La primera es la presa por defecto
            platoMenuPresaRepository.save(asociacion);
        }
    }

    private CatalogoPlatoMenuResponseDTO convertirAResponseDTO(CatalogoPlatoMenu plato) {
        List<PlatoMenuPresa> presasAsociadas = platoMenuPresaRepository.findByCatalogoPlatoMenuId(plato.getId());

        List<PresaResponseDTO> presasDTO = presasAsociadas.stream()
                .map(pmp -> PresaResponseDTO.builder()
                        .id(pmp.getPresa().getId())
                        .nombre(pmp.getPresa().getNombre())
                        .precioAdicional(pmp.getPresa().getPrecioAdicional())
                        .porDefecto(pmp.getPresaPorDefecto())
                        .build())
                .collect(Collectors.toList());

        return CatalogoPlatoMenuResponseDTO.builder()
                .id(plato.getId())
                .nombre(plato.getNombre())
                .tipoMenu(plato.getTipoMenu().name())
                .permiteCambioPresa(plato.getPermiteCambioPresa())
                .activo(plato.getActivo())
                .presasAsociadas(plato.getPermiteCambioPresa() ? presasDTO : null)
                .build();
    }
}