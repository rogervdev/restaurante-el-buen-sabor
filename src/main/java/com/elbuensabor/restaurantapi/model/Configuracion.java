package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "precio_menu", nullable = false)
    private Double precioMenu;

    @Column(name = "costo_descartable")
    private Double costoDescartable = 2.00;

    @Column(name = "costo_delivery")
    private Double costoDelivery = 2.00;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}