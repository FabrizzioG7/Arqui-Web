package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.HistorialReporte;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entrada del historial de cambios de un reporte")
public class HistorialReporteDTO {

    @Schema(description = "ID autogenerado", example = "1")
    private Integer pkHistorialId;

    @Schema(description = "ID del reporte", example = "1")
    private Integer reporteId;

    @Schema(description = "Estado anterior del reporte", example = "pendiente")
    private String estadoAnterior;

    @Schema(description = "Estado nuevo del reporte", example = "en_proceso")
    private String estadoNuevo;

    @Schema(description = "Comentario del cambio", example = "Se asignó inspector")
    private String comentario;

    @Schema(description = "Fecha del cambio")
    private LocalDateTime fechaCambio;

    public static HistorialReporteDTO fromEntity(HistorialReporte h) {
        return new HistorialReporteDTO(
                h.getPkHistorialId(),
                h.getReporte() != null ? h.getReporte().getPkReporteId() : null,
                h.getEstadoAnterior(),
                h.getEstadoNuevo(),
                h.getComentario(),
                h.getFechaCambio()
        );
    }

}
