package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.Ubicacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de una ubicación geográfica")
public class UbicacionDTO {

    @Schema(description = "ID autogenerado", example = "1")
    private Integer pkUbicacionId;

    @NotBlank(message = "La ubicación es obligatoria")
    @Schema(description = "Descripción de la ubicación", example = "Av. Javier Prado Este 123")
    private String ubicacion;

    @NotBlank(message = "El distrito es obligatorio")
    @Schema(description = "Nombre del distrito", example = "Miraflores")
    private String distrito;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "Longitud mínima es -180")
    @DecimalMax(value = "180.0", message = "Longitud máxima es 180")
    @Schema(description = "Coordenada de longitud", example = "-77.028228")
    private BigDecimal longitud;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "Latitud mínima es -90")
    @DecimalMax(value = "90.0", message = "Latitud máxima es 90")
    @Schema(description = "Coordenada de latitud", example = "-12.121898")
    private BigDecimal latitud;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    public static UbicacionDTO fromEntity(Ubicacion u) {
        return new UbicacionDTO(
                u.getPkUbicacionId(),
                u.getUbicacion(),
                u.getDistrito(),
                u.getLongitud(),
                u.getLatitud(),
                u.getCreatedAt()
        );
    }

    public Ubicacion toEntity() {
        Ubicacion u = new Ubicacion();
        u.setUbicacion(this.ubicacion);
        u.setDistrito(this.distrito);
        u.setLongitud(this.longitud);
        u.setLatitud(this.latitud);
        return u;
    }
}
