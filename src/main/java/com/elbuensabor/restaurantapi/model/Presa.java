package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "presa")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Presa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Builder.Default
    @Column(name = "precio_adicional")
    private Double precioAdicional = 0.00;

    @Builder.Default
    private Boolean activo = true;
}