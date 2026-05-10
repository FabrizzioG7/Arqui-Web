package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.JwtRequestDTO;
import com.noistop.noistop.dtos.JwtResponseDTO;
import com.noistop.noistop.security.JwtTokenUtil;
import com.noistop.noistop.security.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Tag(name = "Autenticación", description = "Login con credenciales para obtener el token JWT")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Operation(
            summary = "Iniciar sesión (Login)",
            description = "Envía email y password. Si las credenciales son correctas, retorna un token JWT " +
                    "que debes incluir en el header de tus próximas requests: Authorization: Bearer <token>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso - retorna el token JWT"),
            @ApiResponse(responseCode = "400", description = "Campos vacíos o formato de email inválido"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario inactivo")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody JwtRequestDTO request) throws Exception {
        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USUARIO_DESHABILITADO: La cuenta está inactiva.", e);
        } catch (BadCredentialsException e) {
            throw new Exception("CREDENCIALES_INVALIDAS: Email o contraseña incorrectos.", e);
        }
    }
}
