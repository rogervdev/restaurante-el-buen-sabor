package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "cliente_nombre", length = 100, nullable = false)
    private String clienteNombre;

    @Column(name = "cliente_telefono", length = 15)
    private String clienteTelefono;

    @Column(nullable = false, length = 20)
    private String tipo; // MESA, LLEVAR

    @Column(name = "tipo_venta", nullable = false, length = 20)
    private String tipoVenta; // MENU, CARTA

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private Double total;

    @Column(name = "metodo_pago", nullable = false, length = 20)
    private String metodoPago = "EFECTIVO";

    @Column(name = "monto_recibido")
    private Double montoRecibido;

    @Column(name = "vuelto")
    private Double vuelto = 0.00;

    @Column(name = "costo_descartable")
    private Double costoDescartable = 0.00;

    @Column(name = "requiere_delivery")
    private Boolean requiereDelivery = false;

    @Column(name = "costo_delivery")
    private Double costoDelivery = 0.00;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoDetalle> detalles;
}