package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "catalogo_plato_carta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoPlatoCarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock = 0;

    private Boolean activo = true;

    public enum Categoria {
        SOPA, ENTRADA, PLATO_FONDO, BEBIDA, POSTRE
    }
}