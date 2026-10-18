package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.ClienteRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.ClienteResponseDTO;
import com.elbuensabor.restaurantapi.model.Cliente;
import com.elbuensabor.restaurantapi.repository.ClienteRepository;
import com.elbuensabor.restaurantapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository; // ✅ NUEVO

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return convertirAResponseDTO(cliente);
    }

    public ClienteResponseDTO buscarPorTelefono(String telefono) {
        Cliente cliente = clienteRepository.findByTelefono(telefono)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con teléfono: " + telefono));
        return convertirAResponseDTO(cliente);
    }

    public ClienteResponseDTO crear(ClienteRequestDTO request) {
        if (clienteRepository.findByTelefono(request.getTelefono()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con el teléfono: " + request.getTelefono());
        }

        Cliente cliente = new Cliente();
        cliente.setTelefono(request.getTelefono());
        cliente.setNombre(request.getNombre());
        cliente.setNotas(request.getNotas());
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirAResponseDTO(clienteGuardado);
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        cliente.setNombre(request.getNombre());
        cliente.setNotas(request.getNotas());
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertirAResponseDTO(clienteActualizado);
    }

    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        long pedidosAsociados = pedidoRepository.countByClienteId(id);
        if (pedidosAsociados > 0) {
            throw new RuntimeException(
                    "No se puede eliminar el cliente '" + cliente.getNombre() +
                            "' porque tiene " + pedidosAsociados + " pedido(s) asociado(s). " +
                            "Primero debe anular esos pedidos." // ✅ Texto corregido
            );
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO convertirAResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .telefono(cliente.getTelefono())
                .nombre(cliente.getNombre())
                .notas(cliente.getNotas())
                .build();
    }
}