package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.PresaDia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresaDiaRepository extends JpaRepository<PresaDia, Long> {

    List<PresaDia> findByFecha(LocalDate fecha);

    //@Query con JPQL explícito
    @Query("SELECT pd FROM PresaDia pd WHERE pd.presa.id = :presaId AND pd.fecha = :fecha")
    Optional<PresaDia> buscarPorPresaYFecha(@Param("presaId") Long presaId, @Param("fecha") LocalDate fecha);

    @Modifying
    @Transactional
    @Query("DELETE FROM PresaDia pd WHERE pd.fecha = :fecha")
    void deleteByFecha(@Param("fecha") LocalDate fecha);
}