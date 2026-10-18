package com.elbuensabor.restaurantapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurarMenuDiaRequestDTO {

    @NotNull(message = "La lista de platos es obligatoria")
    private List<PlatoDiaItemDTO> platos;

    @NotNull(message = "La lista de presas es obligatoria")
    private List<PresaDiaItemDTO> presas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatoDiaItemDTO {
        @NotNull(message = "El ID del plato es obligatorio")
        private Long catalogoPlatoMenuId;

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
        private Integer stock;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PresaDiaItemDTO {
        @NotNull(message = "El ID de la presa es obligatorio")
        private Long presaId;

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
        private Integer stock;
    }
}