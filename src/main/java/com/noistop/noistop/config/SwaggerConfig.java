package com.noistop.noistop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI noistopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NoiStop API")
                        .description("API REST para gestión de reportes de contaminación sonora. " +
                                "**Autenticación:** primero llama a `POST /login` con tu email y password " +
                                "para obtener el token JWT. Luego haz clic en el botón 'Authorize' " +
                                "e ingresa: `Bearer <tu_token>` para acceder a los endpoints protegidos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo NoiStop")
                                .email("noistop@ejemplo.com")))
                // Definir el esquema de seguridad Bearer JWT
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el token JWT obtenido del endpoint /login. " +
                                        "Ejemplo: eyJhbGciOiJIUzUxMiJ9...")));
    }
}
