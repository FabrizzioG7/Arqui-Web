package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.AccionAdministrativa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de una acción administrativa sobre un reporte")
public class AccionAdministrativaDTO {

    @Schema(description = "ID autogenerado de la acción", example = "1")
    private Integer pkAccionId;

    @NotBlank(message = "El detalle de la acción es obligatorio")
    @Schema(description = "Descripción/comentario de la acción tomada", example = "Se envió equipo de inspección al lugar del incidente")
    private String detalle;

    @Schema(description = "Fecha en que se realizó la acción")
    private LocalDateTime fechaAccion;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario (autoridad) que registra la acción", example = "2")
    private Integer usuarioId;

    @Schema(description = "Nombre del usuario que registró la acción")
    private String nombreUsuario;

    @NotNull(message = "El ID del reporte es obligatorio")
    @Schema(description = "ID del reporte al que corresponde la acción", example = "1")
    private Integer reporteId;

    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    public static AccionAdministrativaDTO fromEntity(AccionAdministrativa a) {
        return new AccionAdministrativaDTO(
                a.getPkAccionId(),
                a.getDetalle(),
                a.getFechaAccion(),
                a.getUsuario() != null ? a.getUsuario().getPkUsuarioId() : null,
                a.getUsuario() != null ? a.getUsuario().getNombre() : null,
                a.getReporte() != null ? a.getReporte().getPkReporteId() : null,
                a.getCreatedAt()
        );
    }
}
