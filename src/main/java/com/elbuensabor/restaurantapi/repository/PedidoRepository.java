package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    long countByClienteId(Long clienteId);
    List<Pedido> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime inicio, LocalDateTime fin);
}