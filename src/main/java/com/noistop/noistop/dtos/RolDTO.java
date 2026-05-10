package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de un rol del sistema")
public class RolDTO {

    @Schema(description = "ID autogenerado del rol", example = "1")
    private Integer pkRolId;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombreRol;

    public static RolDTO fromEntity(Rol rol) {
        return new RolDTO(rol.getPkRolId(), rol.getNombreRol());
    }

    public Rol toEntity() {
        Rol rol = new Rol();
        rol.setNombreRol(this.nombreRol);
        return rol;
    }
}
