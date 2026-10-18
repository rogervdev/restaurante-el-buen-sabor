package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "catalogo_plato_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoPlatoMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    // ⚠️ ESTA LÍNEA ES LA CLAVE:
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_menu", nullable = false, length = 50)
    private TipoMenu tipoMenu;

    @Column(name = "permite_cambio_presa")
    private Boolean permiteCambioPresa = false;

    private Boolean activo = true;

    @OneToMany(mappedBy = "catalogoPlatoMenu")
    private List<PlatoMenuPresa> platoMenuPresas;

    public enum TipoMenu {
        ENTRADA, SEGUNDO, BEBIDA
    }
}