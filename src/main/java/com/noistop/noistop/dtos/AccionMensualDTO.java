package com.noistop.noistop.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Total de acciones administrativas agrupadas por mes y autoridad")
public class AccionMensualDTO {

    @Schema(description = "Mes en formato YYYY-MM", example = "2026-06")
    private String mes;

    @Schema(example = "Fiscalización Municipal")
    private String autoridad;

    @Schema(description = "Total de acciones registradas en ese mes por esa autoridad", example = "5")
    private Long total;
}