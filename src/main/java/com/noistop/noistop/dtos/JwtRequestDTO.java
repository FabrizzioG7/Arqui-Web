package com.noistop.noistop.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Schema(description = "Credenciales para iniciar sesión")
public class JwtRequestDTO implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @NotBlank(message = "El usuario o correo es obligatorio")
    @Schema(description = "Nombre de usuario o email del usuario registrado", example = "admin@noistop.com")
    private String identificador;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    public JwtRequestDTO() {}

    public JwtRequestDTO(String identificador, String password) {
        this.identificador = identificador;
        this.password = password;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
