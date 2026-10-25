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
@XmlRootElement(name = "ConsultarMenuDelDiaResponse", namespace = "http://elbuensabor.com/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsultarMenuDelDiaResponse {

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private String fecha;

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private int totalPlatos;

    @XmlElement(namespace = "http://elbuensabor.com/schema")
    private String mensaje;
}