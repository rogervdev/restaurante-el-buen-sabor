package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "plato_menu_dia", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"catalogo_plato_menu_id", "fecha"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoMenuDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "catalogo_plato_menu_id", nullable = false)
    private CatalogoPlatoMenu catalogoPlatoMenu;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Integer stock = 0;

    private Boolean disponible = true;
}