package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "catalogo_plato_carta")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoPlatoCarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Builder.Default
    @Column(nullable = false)
    private Integer stock = 0;

    @Builder.Default
    private Boolean activo = true;
}