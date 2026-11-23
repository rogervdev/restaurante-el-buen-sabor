package com.elbuensabor.restaurantapi.service;

import com.elbuensabor.restaurantapi.dto.response.RucResponseDTO;
import com.elbuensabor.restaurantapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Service
public class ConsultaRucService {

    private final RestClient restClient;
    private final String url;
    private final String token;

    public ConsultaRucService(
            @Value("${app.decolecta.url}") String url,
            @Value("${app.decolecta.token}") String token) {
        this.restClient = RestClient.builder().build();
        this.url = url;
        this.token = token;
    }

    public RucResponseDTO consultarRuc(String ruc) {
        String rucLimpio = ruc != null ? ruc.trim() : "";

        if (!rucLimpio.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe tener 11 dígitos numéricos");
        }

        try {
            Map<String, Object> response = restClient.get()
                    .uri(url + "?numero={ruc}", rucLimpio)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response == null) {
                throw new ResourceNotFoundException("SUNAT no devolvió información para el RUC " + rucLimpio);
            }

            return RucResponseDTO.builder()
                    .ruc(asString(response.get("numero_documento"), rucLimpio))
                    .razonSocial(asString(response.get("razon_social"), ""))
                    .direccion(asString(response.get("direccion"), ""))
                    .estado(asString(response.get("estado"), ""))
                    .build();

        } catch (RestClientResponseException ex) {
            int status = ex.getStatusCode().value();

            if (status == 422) {
                throw new ResourceNotFoundException("RUC no válido o no encontrado en SUNAT");
            }
            if (status == 401 || status == 403) {
                throw new IllegalStateException("No se pudo consultar SUNAT: token de Decolecta inválido o no configurado");
            }
            if (status == 400) {
                throw new IllegalArgumentException("Solicitud de consulta RUC inválida");
            }
            throw new IllegalStateException("Error al consultar el RUC (HTTP " + status + ")");
        } catch (RestClientException ex) {
            throw new IllegalStateException("No se pudo conectar con el servicio de consulta RUC");
        }
    }

    private String asString(Object value, String fallback) {
        return value != null ? String.valueOf(value) : fallback;
    }
}
