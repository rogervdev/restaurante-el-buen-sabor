package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.Presa;
import com.elbuensabor.restaurantapi.model.PresaDia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresaRepository extends JpaRepository<Presa, Long> {

    @Query("SELECT pd FROM PresaDia pd JOIN FETCH pd.presa WHERE pd.fecha = :fecha")
    List<PresaDia> buscarConRelaciones(@Param("fecha") LocalDate fecha);


}