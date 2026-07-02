package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.JwtRequestDTO;
import com.noistop.noistop.dtos.JwtResponseDTO;
import com.noistop.noistop.entities.Usuario;
import com.noistop.noistop.security.JwtTokenUtil;
import com.noistop.noistop.security.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin
@Tag(name = "Autenticación", description = "Login con usuario/correo y contraseña para obtener el token JWT")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Operation(
            summary = "Iniciar sesión (Login)",
            description = "Envía identificador (nombre de usuario o email) y password. Si las credenciales son " +
                    "correctas, retorna un token JWT que debes incluir en el header de tus próximas requests: " +
                    "Authorization: Bearer <token>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso - retorna el token JWT y los datos del usuario"),
            @ApiResponse(responseCode = "400", description = "Campos vacíos"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequestDTO request) {
        try {
            authenticate(request.getIdentificador(), request.getPassword());
        } catch (DisabledException e) {
            return errorResponse(HttpStatus.UNAUTHORIZED, "La cuenta está inactiva.");
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return errorResponse(HttpStatus.UNAUTHORIZED, "Usuario/correo o contraseña incorrectos.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getIdentificador());
        final Usuario usuario = userDetailsService.obtenerUsuario(request.getIdentificador());
        final String token = jwtTokenUtil.generateToken(userDetails);

        JwtResponseDTO respuesta = new JwtResponseDTO(
                token,
                usuario.getPkUsuarioId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getNombreRol()
        );
        return ResponseEntity.ok(respuesta);
    }

    private void authenticate(String identificador, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identificador, password));
    }

    private ResponseEntity<Map<String, Object>> errorResponse(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", "No autorizado");
        body.put("message", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}
