package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.PlatoMenuPresa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatoMenuPresaRepository extends JpaRepository<PlatoMenuPresa, Long> {
    List<PlatoMenuPresa> findByCatalogoPlatoMenuId(Long catalogoPlatoMenuId);


    //@Modifying y @Query para DELETE explícito
    @Modifying
    @Transactional
    @Query("DELETE FROM PlatoMenuPresa pmp WHERE pmp.catalogoPlatoMenu.id = :catalogoPlatoMenuId")
    void deleteByCatalogoPlatoMenuId(@Param("catalogoPlatoMenuId") Long catalogoPlatoMenuId);
}
