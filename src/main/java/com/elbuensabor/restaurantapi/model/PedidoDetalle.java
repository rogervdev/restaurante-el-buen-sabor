package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedido_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "catalogo_plato_menu_id")
    private CatalogoPlatoMenu catalogoPlatoMenu; // El SEGUNDO (obligatorio en MENÚ)

    @ManyToOne
    @JoinColumn(name = "catalogo_plato_carta_id")
    private CatalogoPlatoCarta catalogoPlatoCarta; // Para pedidos a la CARTA

    @ManyToOne
    @JoinColumn(name = "presa_id")
    private Presa presa; // Cambio de presa (opcional)

    // ✅ NUEVOS CAMPOS: Entrada y bebida del menú
    @ManyToOne
    @JoinColumn(name = "entrada_id")
    private CatalogoPlatoMenu entrada; // Entrada del menú (opcional)

    @ManyToOne
    @JoinColumn(name = "bebida_id")
    private CatalogoPlatoMenu bebida; // Bebida del menú (opcional)

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(length = 200)
    private String observacion;
}