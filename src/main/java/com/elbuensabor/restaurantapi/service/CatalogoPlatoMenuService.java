package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.CatalogoPlatoMenuRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.CatalogoPlatoMenuResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import com.elbuensabor.restaurantapi.mapper.CatalogoPlatoMenuMapper;
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

@Service
@RequiredArgsConstructor
public class CatalogoPlatoMenuService {

    private final CatalogoPlatoMenuRepository catalogoPlatoMenuRepository;
    private final PlatoMenuPresaRepository platoMenuPresaRepository;
    private final PresaRepository presaRepository;
    private final CatalogoPlatoMenuMapper catalogoPlatoMenuMapper;

    public List<CatalogoPlatoMenuResponseDTO> listarTodos() {
        return catalogoPlatoMenuMapper.toResponseDTOList(catalogoPlatoMenuRepository.findAll());
    }

    public CatalogoPlatoMenuResponseDTO obtenerPorId(Long id) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato del menú no encontrado con ID: " + id));
        return catalogoPlatoMenuMapper.toResponseDTO(plato);
    }

    @Transactional
    public CatalogoPlatoMenuResponseDTO crear(CatalogoPlatoMenuRequestDTO request) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuMapper.toEntity(request);

        CatalogoPlatoMenu platoGuardado = catalogoPlatoMenuRepository.save(plato);

        // Si permite cambio de presa, asociar las presas
        if (Boolean.TRUE.equals(request.getPermiteCambioPresa()) && request.getPresaIds() != null) {
            asociarPresas(platoGuardado, request.getPresaIds());
        }

        return catalogoPlatoMenuMapper.toResponseDTO(platoGuardado);
    }


    @Transactional
    public CatalogoPlatoMenuResponseDTO cambiarEstado(Long id) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato del menú no encontrado con ID: " + id));

        plato.setActivo(!Boolean.TRUE.equals(plato.getActivo())); // Invierte el estado
        return catalogoPlatoMenuMapper.toResponseDTO(catalogoPlatoMenuRepository.save(plato));
    }

    @Transactional
    public CatalogoPlatoMenuResponseDTO actualizar(Long id, CatalogoPlatoMenuRequestDTO request) {
        CatalogoPlatoMenu plato = catalogoPlatoMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato del menú no encontrado con ID: " + id));

        catalogoPlatoMenuMapper.updateEntity(plato, request);

        // Eliminar asociaciones antiguas de presas
        platoMenuPresaRepository.deleteByCatalogoPlatoMenuId(id);

        // Si permite cambio de presa, asociar las nuevas presas
        if (Boolean.TRUE.equals(request.getPermiteCambioPresa()) && request.getPresaIds() != null) {
            asociarPresas(plato, request.getPresaIds());
        }

        CatalogoPlatoMenu platoActualizado = catalogoPlatoMenuRepository.save(plato);
        return catalogoPlatoMenuMapper.toResponseDTO(platoActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!catalogoPlatoMenuRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plato del menú no encontrado con ID: " + id);
        }
        catalogoPlatoMenuRepository.deleteById(id);
    }

    private void asociarPresas(CatalogoPlatoMenu plato, List<Long> presaIds) {
        for (int i = 0; i < presaIds.size(); i++) {
            Long presaId = presaIds.get(i);
            Presa presa = presaRepository.findById(presaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Presa no encontrada con ID: " + presaId));

            PlatoMenuPresa asociacion = PlatoMenuPresa.builder()
                    .catalogoPlatoMenu(plato)
                    .presa(presa)
                    .presaPorDefecto(i == 0) // La primera es la presa por defecto
                    .build();
            platoMenuPresaRepository.save(asociacion);
        }
    }
}
