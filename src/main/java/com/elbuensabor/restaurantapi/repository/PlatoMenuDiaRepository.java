package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.PlatoMenuDia;
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
public interface PlatoMenuDiaRepository extends JpaRepository<PlatoMenuDia, Long> {

    List<PlatoMenuDia> findByFecha(LocalDate fecha);

    // @Query con JPQL explícito
    @Query("SELECT pmd FROM PlatoMenuDia pmd WHERE pmd.catalogoPlatoMenu.id = :catalogoPlatoMenuId AND pmd.fecha = :fecha")
    Optional<PlatoMenuDia> buscarPorPlatoYFecha(@Param("catalogoPlatoMenuId") Long catalogoPlatoMenuId, @Param("fecha") LocalDate fecha);

    //Agregamos la anotacion corregir el problema de concurrencia
    @Modifying
    @Transactional
    @Query("DELETE FROM PlatoMenuDia pmd WHERE pmd.fecha = :fecha")
    void deleteByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT pmd FROM PlatoMenuDia pmd " +
            "JOIN FETCH pmd.catalogoPlatoMenu cpm " +
            "LEFT JOIN FETCH cpm.platoMenuPresas pmp " +
            "LEFT JOIN FETCH pmp.presa " +
            "WHERE pmd.fecha = :fecha")
    List<PlatoMenuDia> buscarConRelaciones(@Param("fecha") LocalDate fecha);
}