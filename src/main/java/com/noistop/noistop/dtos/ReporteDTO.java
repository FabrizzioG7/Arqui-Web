package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.Reporte;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de un reporte de ruido")
public class ReporteDTO {

    @Schema(description = "ID autogenerado del reporte", example = "1")
    private Integer pkReporteId;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción del incidente de ruido", example = "Música a alto volumen proveniente del departamento 5B")
    private String descripcion;

    @Schema(description = "Estado del reporte", example = "pendiente")
    private String estado;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario que crea el reporte", example = "1")
    private Integer usuarioId;

    @Schema(description = "Nombre del usuario")
    private String nombreUsuario;

    @Schema(description = "ID de la medición asociada", example = "1")
    private Integer medicionId;

    @NotNull(message = "El ID de la categoría es obligatorio")
    @Schema(description = "ID de la categoría de ruido", example = "1")
    private Integer categoriaId;

    @Schema(description = "Nombre de la categoría")
    private String nombreCategoria;

    @NotNull(message = "El ID de la ubicación es obligatorio")
    @Schema(description = "ID de la ubicación", example = "1")
    private Integer ubicacionId;

    @Schema(description = "Nombre del distrito")
    private String distrito;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Lista de evidencias del reporte")
    private List<EvidenciaReporteDTO> evidencias;

    public static ReporteDTO fromEntity(Reporte r) {
        ReporteDTO dto = new ReporteDTO();
        dto.setPkReporteId(r.getPkReporteId());
        dto.setDescripcion(r.getDescripcion());
        dto.setEstado(r.getEstado());
        dto.setUsuarioId(r.getUsuario() != null ? r.getUsuario().getPkUsuarioId() : null);
        dto.setNombreUsuario(r.getUsuario() != null ? r.getUsuario().getNombre() : null);
        dto.setMedicionId(r.getMedicion() != null ? r.getMedicion().getPkMedicionId() : null);
        dto.setCategoriaId(r.getCategoriaRuido() != null ? r.getCategoriaRuido().getPkCategoriaId() : null);
        dto.setNombreCategoria(r.getCategoriaRuido() != null ? r.getCategoriaRuido().getNombreCategoria() : null);
        dto.setUbicacionId(r.getUbicacion() != null ? r.getUbicacion().getPkUbicacionId() : null);
        dto.setDistrito(r.getUbicacion() != null ? r.getUbicacion().getDistrito() : null);
        dto.setCreatedAt(r.getCreatedAt());
        if (r.getEvidencias() != null) {
            dto.setEvidencias(r.getEvidencias().stream()
                    .map(EvidenciaReporteDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
