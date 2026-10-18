package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.request.PedidoDetalleRequestDTO;
import com.elbuensabor.restaurantapi.dto.request.PedidoRequestDTO;
import com.elbuensabor.restaurantapi.dto.response.PedidoDetalleResponseDTO;
import com.elbuensabor.restaurantapi.dto.response.PedidoResponseDTO;
import com.elbuensabor.restaurantapi.model.*;
import com.elbuensabor.restaurantapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // ✅ 1. Importar Slf4j para los logs
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // ✅ 2. Agregar esta anotación para habilitar el objeto 'log'
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final PlatoMenuDiaRepository platoMenuDiaRepository;
    private final PresaDiaRepository presaDiaRepository;
    private final CatalogoPlatoCartaRepository platoCartaRepository;
    private final PresaRepository presaRepository;
    private final CatalogoPlatoMenuRepository platoMenuRepository;
    private final ConfiguracionRepository configuracionRepository;

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        // ✅ 3. Log de entrada: Muestra el HILO, tipo de venta y cuántos detalles llegan
        log.info("🚀 [HILO: {}] INICIO crearPedido - Tipo: {}, Venta: {}, Detalles: {}",
                Thread.currentThread().getName(), request.getTipo(), request.getTipoVenta(), request.getDetalles().size());

        try {
            // 1. Buscar o crear cliente
            Cliente cliente = null;
            if (request.getClienteTelefono() != null && !request.getClienteTelefono().isEmpty()) {
                cliente = clienteRepository.findByTelefono(request.getClienteTelefono()).orElse(null);
                if (cliente == null) {
                    cliente = new Cliente();
                    cliente.setTelefono(request.getClienteTelefono());
                    cliente.setNombre(request.getClienteNombre());
                    cliente = clienteRepository.save(cliente);
                    log.debug("👤 [HILO: {}] Cliente nuevo creado: {}", Thread.currentThread().getName(), cliente.getTelefono());
                }
            }

            // 2. Obtener configuración
            Configuracion config = configuracionRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay configuración del restaurante"));

            double precioMenu = config.getPrecioMenu();
            double costoDescartableUnit = config.getCostoDescartable() != null ? config.getCostoDescartable() : 2.00;
            double costoDeliveryUnit = config.getCostoDelivery() != null ? config.getCostoDelivery() : 2.00;

            // 3. Crear el pedido padre
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setClienteNombre(request.getClienteNombre());
            pedido.setClienteTelefono(request.getClienteTelefono());
            pedido.setTipo(request.getTipo().toUpperCase());
            pedido.setTipoVenta(request.getTipoVenta().toUpperCase());
            pedido.setEstado("PENDIENTE");
            pedido.setFechaHora(LocalDateTime.now());
            pedido.setRequiereDelivery(request.getRequiereDelivery() != null ? request.getRequiereDelivery() : false);

            double subtotalPlatos = 0.0;
            int cantidadSegundos = 0;
            List<PedidoDetalle> detallesGuardados = new ArrayList<>();
            LocalDate hoy = LocalDate.now();

            // 4. Procesar cada detalle del pedido
            for (PedidoDetalleRequestDTO detalleReq : request.getDetalles()) {
                log.debug("⚙️ [HILO: {}] Procesando detalle: PlatoId={}, CartaId={}, Cantidad={}",
                        Thread.currentThread().getName(), detalleReq.getPlatoId(), detalleReq.getCatalogoPlatoCartaId(), detalleReq.getCantidad());

                PedidoDetalle detalle = new PedidoDetalle();
                detalle.setPedido(pedido);
                detalle.setCantidad(detalleReq.getCantidad());
                detalle.setObservacion(detalleReq.getObservacion());

                if ("MENU".equals(pedido.getTipoVenta())) {
                    // --- LÓGICA PARA MENÚ ---
                    CatalogoPlatoMenu segundo = platoMenuRepository.findById(detalleReq.getPlatoId())
                            .orElseThrow(() -> new RuntimeException("Segundo no encontrado con ID: " + detalleReq.getPlatoId()));

                    if (!"SEGUNDO".equals(segundo.getTipoMenu().name())) {
                        throw new RuntimeException("El plato con ID " + detalleReq.getPlatoId() + " no es un SEGUNDO.");
                    }

                    detalle.setCatalogoPlatoMenu(segundo);
                    detalle.setPrecioUnitario(precioMenu);
                    subtotalPlatos += precioMenu * detalleReq.getCantidad();
                    cantidadSegundos += detalleReq.getCantidad();

                    if (detalleReq.getEntradaId() != null) {
                        CatalogoPlatoMenu entrada = platoMenuRepository.findById(detalleReq.getEntradaId())
                                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + detalleReq.getEntradaId()));
                        detalle.setEntrada(entrada);

                        log.debug("📉 [HILO: {}] Descontando stock entrada: {}", Thread.currentThread().getName(), entrada.getNombre());
                        Optional<PlatoMenuDia> optEntradaDia = platoMenuDiaRepository.buscarPorPlatoYFecha(entrada.getId(), hoy);
                        optEntradaDia.ifPresent(entradaDia -> {
                            if (entradaDia.getStock() < detalleReq.getCantidad()) {
                                throw new RuntimeException("Stock insuficiente para la entrada: " + entrada.getNombre());
                            }
                            entradaDia.setStock(entradaDia.getStock() - detalleReq.getCantidad());
                            platoMenuDiaRepository.save(entradaDia);
                        });
                    }

                    if (detalleReq.getBebidaId() != null) {
                        CatalogoPlatoMenu bebida = platoMenuRepository.findById(detalleReq.getBebidaId())
                                .orElseThrow(() -> new RuntimeException("Bebida no encontrada con ID: " + detalleReq.getBebidaId()));
                        detalle.setBebida(bebida);

                        log.debug("📉 [HILO: {}] Descontando stock bebida: {}", Thread.currentThread().getName(), bebida.getNombre());
                        Optional<PlatoMenuDia> optBebidaDia = platoMenuDiaRepository.buscarPorPlatoYFecha(bebida.getId(), hoy);
                        optBebidaDia.ifPresent(bebidaDia -> {
                            if (bebidaDia.getStock() < detalleReq.getCantidad()) {
                                throw new RuntimeException("Stock insuficiente para la bebida: " + bebida.getNombre());
                            }
                            bebidaDia.setStock(bebidaDia.getStock() - detalleReq.getCantidad());
                            platoMenuDiaRepository.save(bebidaDia);
                        });
                    }

                    log.debug("📉 [HILO: {}] Descontando stock segundo: {}", Thread.currentThread().getName(), segundo.getNombre());
                    Optional<PlatoMenuDia> optSegundoDia = platoMenuDiaRepository.buscarPorPlatoYFecha(segundo.getId(), hoy);
                    PlatoMenuDia segundoDia = optSegundoDia.orElseThrow(() ->
                            new RuntimeException("No hay stock registrado para el segundo: " + segundo.getNombre() + " hoy"));

                    if (segundoDia.getStock() < detalleReq.getCantidad()) {
                        throw new RuntimeException("Stock insuficiente para: " + segundo.getNombre());
                    }
                    segundoDia.setStock(segundoDia.getStock() - detalleReq.getCantidad());
                    platoMenuDiaRepository.save(segundoDia);

                    if (detalleReq.getPresaId() != null) {
                        Presa presa = presaRepository.findById(detalleReq.getPresaId())
                                .orElseThrow(() -> new RuntimeException("Presa no encontrada con ID: " + detalleReq.getPresaId()));
                        detalle.setPresa(presa);

                        log.debug("📉 [HILO: {}] Descontando stock presa: {}", Thread.currentThread().getName(), presa.getNombre());
                        Optional<PresaDia> optPresaDia = presaDiaRepository.buscarPorPresaYFecha(presa.getId(), hoy);
                        PresaDia presaDia = optPresaDia.orElseThrow(() ->
                                new RuntimeException("No hay stock de la presa: " + presa.getNombre() + " hoy"));

                        if (presaDia.getStock() < detalleReq.getCantidad()) {
                            throw new RuntimeException("Stock insuficiente para la presa: " + presa.getNombre());
                        }
                        presaDia.setStock(presaDia.getStock() - detalleReq.getCantidad());
                        presaDiaRepository.save(presaDia);
                    }

                } else {
                    // --- LÓGICA PARA CARTA ---
                    if (detalleReq.getCatalogoPlatoCartaId() == null) {
                        throw new RuntimeException("Para pedidos a la carta, debe especificar catalogoPlatoCartaId");
                    }

                    CatalogoPlatoCarta platoCarta = platoCartaRepository.findById(detalleReq.getCatalogoPlatoCartaId())
                            .orElseThrow(() -> new RuntimeException("Plato a la carta no encontrado con ID: " + detalleReq.getCatalogoPlatoCartaId()));

                    detalle.setCatalogoPlatoCarta(platoCarta);
                    detalle.setPrecioUnitario(platoCarta.getPrecio());
                    subtotalPlatos += platoCarta.getPrecio() * detalleReq.getCantidad();

                    log.debug("📉 [HILO: {}] Descontando stock carta: {}", Thread.currentThread().getName(), platoCarta.getNombre());
                    if (platoCarta.getStock() < detalleReq.getCantidad()) {
                        throw new RuntimeException("Stock insuficiente para: " + platoCarta.getNombre());
                    }
                    platoCarta.setStock(platoCarta.getStock() - detalleReq.getCantidad());
                    platoCartaRepository.save(platoCarta);

                    if (detalleReq.getBebidaId() != null) {
                        CatalogoPlatoMenu bebida = platoMenuRepository.findById(detalleReq.getBebidaId())
                                .orElseThrow(() -> new RuntimeException("Bebida no encontrada con ID: " + detalleReq.getBebidaId()));
                        detalle.setBebida(bebida);

                        log.debug("📉 [HILO: {}] Descontando stock bebida (en carta): {}", Thread.currentThread().getName(), bebida.getNombre());
                        Optional<PlatoMenuDia> optBebidaDia = platoMenuDiaRepository.buscarPorPlatoYFecha(bebida.getId(), hoy);
                        optBebidaDia.ifPresent(bebidaDia -> {
                            if (bebidaDia.getStock() < detalleReq.getCantidad()) {
                                throw new RuntimeException("Stock insuficiente para la bebida: " + bebida.getNombre());
                            }
                            bebidaDia.setStock(bebidaDia.getStock() - detalleReq.getCantidad());
                            platoMenuDiaRepository.save(bebidaDia);
                        });
                    }
                }

                detallesGuardados.add(detalle);
            }

            // 5. Calcular costo de descartables (solo LLEVAR + MENÚ)
            double costoDescartable = 0.0;
            if ("LLEVAR".equals(pedido.getTipo()) && "MENU".equals(pedido.getTipoVenta())) {
                costoDescartable = cantidadSegundos * costoDescartableUnit;
            }

            // 6. Calcular costo de delivery (fijo, si requiere)
            double costoDelivery = pedido.getRequiereDelivery() ? costoDeliveryUnit : 0.0;

            // 7. Calcular total
            double totalCalculado = subtotalPlatos + costoDescartable + costoDelivery;

            // 8. Validar método de pago y calcular vuelto
            String metodoPago = request.getMetodoPago().toUpperCase();
            double vuelto = 0.0;
            Double montoRecibido = request.getMontoRecibido();

            if ("EFECTIVO".equals(metodoPago)) {
                if (montoRecibido == null) {
                    throw new RuntimeException("Para pago en efectivo, debe indicar el monto recibido");
                }
                if (montoRecibido < totalCalculado) {
                    throw new RuntimeException("El monto recibido (S/" + montoRecibido + ") es menor al total (S/" + totalCalculado + ")");
                }
                vuelto = montoRecibido - totalCalculado;
            } else {
                montoRecibido = totalCalculado;
            }

            // 9. Guardar pedido
            pedido.setTotal(totalCalculado);
            pedido.setMetodoPago(metodoPago);
            pedido.setMontoRecibido(montoRecibido);
            pedido.setVuelto(vuelto);
            pedido.setCostoDescartable(costoDescartable);
            pedido.setCostoDelivery(costoDelivery);
            pedido.setDetalles(detallesGuardados);

            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            // ✅ 4. Log de éxito
            log.info("✅ [HILO: {}] FIN EXITOSO crearPedido - Pedido ID: {}", Thread.currentThread().getName(), pedidoGuardado.getId());

            // 10. Construir respuesta con los nuevos campos
            List<PedidoDetalleResponseDTO> detallesResponse = pedidoGuardado.getDetalles().stream()
                    .map(detalle -> {
                        PedidoDetalleResponseDTO.PedidoDetalleResponseDTOBuilder builder = PedidoDetalleResponseDTO.builder()
                                .id(detalle.getId())
                                .cantidad(detalle.getCantidad())
                                .precioUnitario(detalle.getPrecioUnitario())
                                .observacion(detalle.getObservacion());

                        if ("MENU".equals(pedidoGuardado.getTipoVenta())) {
                            builder.nombreSegundo(detalle.getCatalogoPlatoMenu() != null ? detalle.getCatalogoPlatoMenu().getNombre() : null);
                            builder.nombreEntrada(detalle.getEntrada() != null ? detalle.getEntrada().getNombre() : null);
                            builder.nombreBebida(detalle.getBebida() != null ? detalle.getBebida().getNombre() : null);
                            builder.nombrePresa(detalle.getPresa() != null ? detalle.getPresa().getNombre() : null);
                        } else {
                            builder.nombrePlatoCarta(detalle.getCatalogoPlatoCarta() != null ? detalle.getCatalogoPlatoCarta().getNombre() : null);
                        }

                        return builder.build();
                    })
                    .collect(Collectors.toList());

            return PedidoResponseDTO.builder()
                    .id(pedidoGuardado.getId())
                    .clienteNombre(pedidoGuardado.getClienteNombre())
                    .clienteTelefono(pedidoGuardado.getClienteTelefono())
                    .tipo(pedidoGuardado.getTipo())
                    .tipoVenta(pedidoGuardado.getTipoVenta())
                    .estado(pedidoGuardado.getEstado())
                    .total(pedidoGuardado.getTotal())
                    .fechaHora(pedidoGuardado.getFechaHora())
                    .metodoPago(pedidoGuardado.getMetodoPago())
                    .montoRecibido(pedidoGuardado.getMontoRecibido())
                    .vuelto(pedidoGuardado.getVuelto())
                    .costoDescartable(pedidoGuardado.getCostoDescartable())
                    .requiereDelivery(pedidoGuardado.getRequiereDelivery())
                    .costoDelivery(pedidoGuardado.getCostoDelivery())
                    .detalles(detallesResponse)
                    .build();

        } catch (Exception e) {
            // ✅ 5. Log de error: Captura la excepción exacta y el hilo que falló
            log.error("❌ [HILO: {}] ERROR en crearPedido: {}", Thread.currentThread().getName(), e.getMessage(), e);
            throw e; // Relanzamos para que el frontend reciba el 400/500
        }
    }

    // ✅ Método actualizado para mapear correctamente los nuevos campos
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        List<PedidoDetalleResponseDTO> detalles = pedido.getDetalles().stream().map(detalle -> {
            PedidoDetalleResponseDTO.PedidoDetalleResponseDTOBuilder builder = PedidoDetalleResponseDTO.builder()
                    .id(detalle.getId())
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(detalle.getPrecioUnitario())
                    .observacion(detalle.getObservacion());

            if ("MENU".equals(pedido.getTipoVenta())) {
                builder.nombreSegundo(detalle.getCatalogoPlatoMenu() != null ? detalle.getCatalogoPlatoMenu().getNombre() : null);
                builder.nombreEntrada(detalle.getEntrada() != null ? detalle.getEntrada().getNombre() : null);
                builder.nombreBebida(detalle.getBebida() != null ? detalle.getBebida().getNombre() : null);
                builder.nombrePresa(detalle.getPresa() != null ? detalle.getPresa().getNombre() : null);
            } else {
                builder.nombrePlatoCarta(detalle.getCatalogoPlatoCarta() != null ? detalle.getCatalogoPlatoCarta().getNombre() : null);
            }

            return builder.build();
        }).collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteNombre(pedido.getClienteNombre())
                .clienteTelefono(pedido.getClienteTelefono())
                .tipo(pedido.getTipo())
                .tipoVenta(pedido.getTipoVenta())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .fechaHora(pedido.getFechaHora())
                .metodoPago(pedido.getMetodoPago())
                .montoRecibido(pedido.getMontoRecibido())
                .vuelto(pedido.getVuelto())
                .costoDescartable(pedido.getCostoDescartable())
                .requiereDelivery(pedido.getRequiereDelivery())
                .costoDelivery(pedido.getCostoDelivery())
                .detalles(detalles)
                .build();
    }

    @Transactional
    public PedidoResponseDTO cambiarEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        String estadoUpper = nuevoEstado.toUpperCase();
        if (!List.of("PENDIENTE", "EN_COCINA", "LISTO", "ENTREGADO", "ANULADO").contains(estadoUpper)) {
            throw new RuntimeException("Estado no válido. Use: PENDIENTE, EN_COCINA, LISTO, ENTREGADO, ANULADO");
        }

        pedido.setEstado(estadoUpper);
        pedidoRepository.save(pedido);
        return obtenerPedidoPorId(id);
    }

    @Transactional
    public PedidoResponseDTO anularPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        if ("ANULADO".equals(pedido.getEstado())) {
            throw new RuntimeException("El pedido ya está anulado.");
        }

        if ("ENTREGADO".equals(pedido.getEstado())) {
            throw new RuntimeException("No se puede anular un pedido que ya fue entregado.");
        }

        // ✅ Devolver stock de TODOS los componentes
        LocalDate hoy = LocalDate.now();
        for (var detalle : pedido.getDetalles()) {
            if ("MENU".equals(pedido.getTipoVenta())) {

                // 1. Devolver stock del SEGUNDO
                Optional<PlatoMenuDia> optSegundoDia = platoMenuDiaRepository.buscarPorPlatoYFecha(
                        detalle.getCatalogoPlatoMenu().getId(), hoy);
                optSegundoDia.ifPresent(segundoDia -> {
                    segundoDia.setStock(segundoDia.getStock() + detalle.getCantidad());
                    platoMenuDiaRepository.save(segundoDia);
                });

                // 2. ✅ NUEVO: Devolver stock de la ENTRADA (si existe)
                if (detalle.getEntrada() != null) {
                    Optional<PlatoMenuDia> optEntradaDia = platoMenuDiaRepository.buscarPorPlatoYFecha(
                            detalle.getEntrada().getId(), hoy);
                    optEntradaDia.ifPresent(entradaDia -> {
                        entradaDia.setStock(entradaDia.getStock() + detalle.getCantidad());
                        platoMenuDiaRepository.save(entradaDia);
                    });
                }

                // 3. ✅ NUEVO: Devolver stock de la BEBIDA (si existe)
                if (detalle.getBebida() != null) {
                    Optional<PlatoMenuDia> optBebidaDia = platoMenuDiaRepository.buscarPorPlatoYFecha(
                            detalle.getBebida().getId(), hoy);
                    optBebidaDia.ifPresent(bebidaDia -> {
                        bebidaDia.setStock(bebidaDia.getStock() + detalle.getCantidad());
                        platoMenuDiaRepository.save(bebidaDia);
                    });
                }

                // 4. Devolver stock de la PRESA (si hubo cambio)
                if (detalle.getPresa() != null) {
                    Optional<PresaDia> optPresaDia = presaDiaRepository.buscarPorPresaYFecha(
                            detalle.getPresa().getId(), hoy);
                    optPresaDia.ifPresent(presaDia -> {
                        presaDia.setStock(presaDia.getStock() + detalle.getCantidad());
                        presaDiaRepository.save(presaDia);
                    });
                }
            } else {
                // Lógica para CARTA

                // 1. Devolver stock del plato a la carta
                var platoCarta = detalle.getCatalogoPlatoCarta();
                if (platoCarta != null) {
                    platoCarta.setStock(platoCarta.getStock() + detalle.getCantidad());
                    platoCartaRepository.save(platoCarta);
                }

                // ✅ 2. NUEVO: Devolver stock de la BEBIDA (si existe en el detalle)
                if (detalle.getBebida() != null) {
                    Optional<PlatoMenuDia> optBebidaDia = platoMenuDiaRepository.buscarPorPlatoYFecha(
                            detalle.getBebida().getId(), hoy);
                    optBebidaDia.ifPresent(bebidaDia -> {
                        bebidaDia.setStock(bebidaDia.getStock() + detalle.getCantidad());
                        platoMenuDiaRepository.save(bebidaDia);
                    });
                }

            }
        }

        // 5. Cambiar estado a ANULADO
        pedido.setEstado("ANULADO");
        pedidoRepository.save(pedido);

        return obtenerPedidoPorId(pedido.getId());
    }
}