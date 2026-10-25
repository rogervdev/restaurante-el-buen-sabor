package com.elbuensabor.restaurantapi.ws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement(name = "ConsultarPedidoResponse", namespace = "http://elbuensabor.com/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsultarPedidoResponse {

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private long pedidoId;

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private String cliente;

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private double total;

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private String estado;

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private String mensaje;
}