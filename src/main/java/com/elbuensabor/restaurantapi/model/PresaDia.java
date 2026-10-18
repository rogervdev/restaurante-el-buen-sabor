package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "presa_dia", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"presa_id", "fecha"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresaDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "presa_id", nullable = false)
    private Presa presa;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Integer stock = 0;
}