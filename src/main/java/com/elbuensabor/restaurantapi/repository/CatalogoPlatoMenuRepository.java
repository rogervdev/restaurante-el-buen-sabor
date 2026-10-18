package com.elbuensabor.restaurantapi.repository;

import com.elbuensabor.restaurantapi.model.CatalogoPlatoMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoPlatoMenuRepository extends JpaRepository<CatalogoPlatoMenu, Long> {
}