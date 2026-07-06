package com.noistop.noistop.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Respuesta del login con el token JWT generado y los datos del usuario")
public class JwtResponseDTO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    @Schema(description = "Token JWT a incluir en el header Authorization de futuras requests",
            example = "eyJhbGciOiJIUzUxMiJ9...")
    private final String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private final String tipo = "Bearer";

    @Schema(description = "ID del usuario autenticado", example = "1")
    private final Integer pkUsuarioId;

    @Schema(description = "Nombre del usuario autenticado", example = "Juan Pérez")
    private final String nombre;

    @Schema(description = "Email del usuario autenticado", example = "juan@email.com")
    private final String email;

    @Schema(description = "Rol del usuario autenticado", example = "ADMIN")
    private final String rol;

    public JwtResponseDTO(String token, Integer pkUsuarioId, String nombre, String email, String rol) {
        this.token = token;
        this.pkUsuarioId = pkUsuarioId;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getPkUsuarioId() {
        return pkUsuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}
