package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.EvidenciaReporte;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de una evidencia de reporte")
public class EvidenciaReporteDTO {

    @Schema(description = "ID autogenerado", example = "1")
    private Integer pkEvidenciaId;

    @Schema(description = "Nombre interno con el que se guardó el archivo en el servidor", example = "9f1c2b3a-....jpg")
    private String rutaArchivo;

    @Schema(description = "Nombre original del archivo tal como lo subió el usuario", example = "foto_ruido.jpg")
    private String nombreOriginal;

    @Schema(description = "URL para ver/descargar la imagen (GET, requiere estar logueado)", example = "/api/evidencias/1/archivo")
    private String urlArchivo;

    @Schema(description = "ID del reporte al que pertenece", example = "1")
    private Integer reporteId;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    public static EvidenciaReporteDTO fromEntity(EvidenciaReporte e) {
        EvidenciaReporteDTO dto = new EvidenciaReporteDTO();
        dto.setPkEvidenciaId(e.getPkEvidenciaId());
        dto.setRutaArchivo(e.getRutaArchivo());
        dto.setNombreOriginal(e.getNombreOriginal());
        dto.setUrlArchivo("/api/evidencias/" + e.getPkEvidenciaId() + "/archivo");
        dto.setReporteId(e.getReporte() != null ? e.getReporte().getPkReporteId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}
