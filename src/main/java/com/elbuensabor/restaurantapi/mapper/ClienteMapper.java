package com.elbuensabor.restaurantapi.mapper;

import com.elbuensabor.restaurantapi.dto.request.ClienteRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.ClienteResponseDTO;
import com.elbuensabor.restaurantapi.model.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Cliente.builder()
                .telefono(dto.getTelefono())
                .nombre(dto.getNombre())
                .notas(dto.getNotas())
                .build();
    }

    public void updateEntity(Cliente cliente, ClienteRequestDTO dto) {
        if (cliente == null || dto == null) {
            return;
        }
        cliente.setTelefono(dto.getTelefono());
        cliente.setNombre(dto.getNombre());
        cliente.setNotas(dto.getNotas());
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .telefono(cliente.getTelefono())
                .nombre(cliente.getNombre())
                .notas(cliente.getNotas())
                .build();
    }

    public List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes) {
        if (clientes == null) {
            return List.of();
        }
        return clientes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
