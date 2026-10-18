package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plato_menu_presa", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"catalogo_plato_menu_id", "presa_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoMenuPresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "catalogo_plato_menu_id", nullable = false)
    private CatalogoPlatoMenu catalogoPlatoMenu;

    @ManyToOne
    @JoinColumn(name = "presa_id", nullable = false)
    private Presa presa;

    @Column(name = "presa_por_defecto")
    private Boolean presaPorDefecto = false;
}