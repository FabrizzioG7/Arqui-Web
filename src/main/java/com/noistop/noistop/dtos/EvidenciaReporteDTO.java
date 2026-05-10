package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.EvidenciaReporte;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de una evidencia de reporte")
public class EvidenciaReporteDTO {

    @Schema(description = "ID autogenerado", example = "1")
    private Integer pkEvidenciaId;

    @NotBlank(message = "La ruta del archivo es obligatoria")
    @Schema(description = "Ruta del archivo de evidencia", example = "/uploads/evidencia_001.jpg")
    private String rutaArchivo;

    @NotNull(message = "El ID del reporte es obligatorio")
    @Schema(description = "ID del reporte al que pertenece", example = "1")
    private Integer reporteId;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    public static EvidenciaReporteDTO fromEntity(EvidenciaReporte e) {
        return new EvidenciaReporteDTO(
                e.getPkEvidenciaId(),
                e.getRutaArchivo(),
                e.getReporte() != null ? e.getReporte().getPkReporteId() : null,
                e.getCreatedAt()
        );
    }

    public EvidenciaReporte toEntity() {
        EvidenciaReporte e = new EvidenciaReporte();
        e.setRutaArchivo(this.rutaArchivo);
        return e;
    }
}
