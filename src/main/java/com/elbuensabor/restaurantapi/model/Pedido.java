package com.elbuensabor.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@Builder
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

    @Builder.Default
    @Column(name = "tipo_comprobante", length = 20)
    private String tipoComprobante = "BOLETA";

    @Column(name = "serie_correlativo", length = 20)
    private String serieCorrelativo;

    @Builder.Default
    @Column(name = "concepto_comprobante", length = 20)
    private String conceptoComprobante = "DETALLADO";

    @Column(name = "cliente_documento", length = 11)
    private String clienteDocumento;

    @Column(name = "cliente_direccion", length = 200)
    private String clienteDireccion;

    @Column(nullable = false, length = 20)
    private String tipo; // MESA, LLEVAR

    @Column(name = "tipo_venta", nullable = false, length = 20)
    private String tipoVenta; // MENU, CARTA

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private Double total;

    @Builder.Default
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private String metodoPago = "EFECTIVO";

    @Column(name = "monto_recibido")
    private Double montoRecibido;

    @Builder.Default
    @Column(name = "vuelto")
    private Double vuelto = 0.00;

    @Builder.Default
    @Column(name = "costo_descartable")
    private Double costoDescartable = 0.00;

    @Builder.Default
    @Column(name = "requiere_delivery")
    private Boolean requiereDelivery = false;

    @Builder.Default
    @Column(name = "costo_delivery")
    private Double costoDelivery = 0.00;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    /**
     * Usuario que registró el pedido. Nullable para no romper pedidos
     * históricos creados antes de habilitar la autenticación.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoDetalle> detalles;
}