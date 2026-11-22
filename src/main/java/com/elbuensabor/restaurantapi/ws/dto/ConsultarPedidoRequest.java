package com.elbuensabor.restaurantapi.ws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "ConsultarPedidoRequest", namespace = "http://elbuensabor.com/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsultarPedidoRequest {

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private long pedidoId;
}