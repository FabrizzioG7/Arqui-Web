package com.noistop.noistop.dtos;

import com.noistop.noistop.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representación de un usuario del sistema")
public class UsuarioDTO {

    @Schema(description = "ID autogenerado", example = "1")
    private Integer pkUsuarioId;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    @Schema(description = "Correo electrónico", example = "juan@email.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @NotNull(message = "El ID del rol es obligatorio")
    @Schema(description = "ID del rol asignado", example = "1")
    private Integer rolId;

    @Schema(description = "Nombre del rol asignado", example = "ADMIN")
    private String nombreRol;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    public static UsuarioDTO fromEntity(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setPkUsuarioId(u.getPkUsuarioId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setPassword(null); // No exponer la contraseña
        dto.setRolId(u.getRol() != null ? u.getRol().getPkRolId() : null);
        dto.setNombreRol(u.getRol() != null ? u.getRol().getNombreRol() : null);
        dto.setCreatedAt(u.getCreatedAt());
        dto.setUpdatedAt(u.getUpdatedAt());
        return dto;
    }
}
