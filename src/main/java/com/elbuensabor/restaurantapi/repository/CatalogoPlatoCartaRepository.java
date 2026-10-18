package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.CatalogoPlatoCarta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoPlatoCartaRepository extends JpaRepository<CatalogoPlatoCarta, Long> {
}