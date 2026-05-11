package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.Medicion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de una medición de decibeles")
public class MedicionDTO {

    @Schema(description = "ID autogenerado de la medición", example = "1")
    private Integer pkMedicionId;

    @NotNull(message = "Los decibeles son obligatorios")
    @Min(value = 0, message = "Los decibeles no pueden ser negativos")
    @Max(value = 200, message = "Los decibeles no pueden superar 200 dB")
    @Schema(description = "Nivel de ruido en decibeles", example = "85")
    private Integer decibeles;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Schema(description = "Fecha y hora de la medición", example = "2024-06-15T22:30:00")
    private LocalDateTime fechaHora;

    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    public static MedicionDTO fromEntity(Medicion m) {
        return new MedicionDTO(
                m.getPkMedicionId(),
                m.getDecibeles(),
                m.getFechaHora(),
                m.getCreatedAt()
        );
    }

    public Medicion toEntity() {
        Medicion m = new Medicion();
        m.setDecibeles(this.decibeles);
        m.setFechaHora(this.fechaHora);
        return m;
    }
}
