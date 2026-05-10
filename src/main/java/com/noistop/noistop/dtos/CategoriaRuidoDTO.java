package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.CategoriaRuido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de una categoría de ruido")
public class CategoriaRuidoDTO {

    @Schema(description = "ID autogenerado de la categoría", example = "1")
    private Integer pkCategoriaId;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Schema(description = "Nombre de la categoría", example = "Tráfico")
    private String nombreCategoria;

    @Schema(description = "Descripción de la categoría", example = "Ruido generado por vehículos y tránsito")
    private String descripcionCategoria;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    public static CategoriaRuidoDTO fromEntity(CategoriaRuido c) {
        return new CategoriaRuidoDTO(
                c.getPkCategoriaId(),
                c.getNombreCategoria(),
                c.getDescripcionCategoria(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }

    public CategoriaRuido toEntity() {
        CategoriaRuido c = new CategoriaRuido();
        c.setNombreCategoria(this.nombreCategoria);
        c.setDescripcionCategoria(this.descripcionCategoria);
        return c;
    }
}
