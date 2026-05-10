package com.noistop.noistop.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Respuesta del login con el token JWT generado")
public class JwtResponseDTO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    @Schema(description = "Token JWT a incluir en el header Authorization de futuras requests",
            example = "eyJhbGciOiJIUzUxMiJ9...")
    private final String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private final String tipo = "Bearer";

    public JwtResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }
}
