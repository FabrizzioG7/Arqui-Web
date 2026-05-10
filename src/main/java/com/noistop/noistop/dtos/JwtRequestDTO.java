package com.noistop.noistop.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Schema(description = "Credenciales para iniciar sesión")
public class JwtRequestDTO implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    @Schema(description = "Email del usuario registrado", example = "admin@noistop.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    public JwtRequestDTO() {}

    public JwtRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
