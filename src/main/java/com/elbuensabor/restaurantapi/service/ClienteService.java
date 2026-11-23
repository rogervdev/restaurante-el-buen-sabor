package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.ClienteRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.ClienteResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import com.elbuensabor.restaurantapi.mapper.ClienteMapper;
import com.elbuensabor.restaurantapi.model.Cliente;
import com.elbuensabor.restaurantapi.repository.ClienteRepository;
import com.elbuensabor.restaurantapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository; // ✅ NUEVO
    private final ClienteMapper clienteMapper;

    public List<ClienteResponseDTO> listarTodos() {
        return clienteMapper.toResponseDTOList(clienteRepository.findAll());
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO buscarPorTelefono(String telefono) {
        Cliente cliente = clienteRepository.findByTelefono(telefono)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con teléfono: " + telefono));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO crear(ClienteRequestDTO request) {
        if (clienteRepository.findByTelefono(request.getTelefono()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con el teléfono: " + request.getTelefono());
        }

        Cliente cliente = clienteMapper.toEntity(request);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteGuardado);
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        clienteMapper.updateEntity(cliente, request);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteActualizado);
    }

    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        long pedidosAsociados = pedidoRepository.countByClienteId(id);
        if (pedidosAsociados > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar el cliente '" + cliente.getNombre() +
                            "' porque tiene " + pedidosAsociados + " pedido(s) asociado(s). " +
                            "Primero debe anular esos pedidos."
            );
        }
        clienteRepository.deleteById(id);
    }
}
