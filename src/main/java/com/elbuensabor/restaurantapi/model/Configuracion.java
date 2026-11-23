package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "precio_menu", nullable = false)
    private Double precioMenu;

    @Builder.Default
    @Column(name = "precio_menu_sin_entrada")
    private Double precioMenuSinEntrada = 0.00;

    @Builder.Default
    @Column(name = "costo_descartable")
    private Double costoDescartable = 2.00;

    @Builder.Default
    @Column(name = "costo_delivery")
    private Double costoDelivery = 2.00;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}