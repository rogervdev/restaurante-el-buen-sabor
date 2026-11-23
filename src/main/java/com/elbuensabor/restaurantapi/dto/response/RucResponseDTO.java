package com.elbuensabor.restaurantapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RucResponseDTO {
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String estado;
}
