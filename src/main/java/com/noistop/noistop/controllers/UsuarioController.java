package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.UsuarioDTO;
import com.noistop.noistop.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "US04-US08: Gestión de usuarios (Crear, Listar, Editar, Eliminar, Buscar por ID)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "US04 - Registrar un nuevo usuario", description = "Crea un usuario. Valida campos obligatorios, formato de email y existencia del rol asignado. Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campos vacíos o formato inválido"),
            @ApiResponse(responseCode = "404", description = "El rol especificado no existe"),
            @ApiResponse(responseCode = "409", description = "El email ya está en uso")
    })
    @PostMapping("/insertar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @Operation(summary = "Registro público de usuario", description = "Crea un usuario desde la pantalla de registro del login. No requiere estar autenticado. Valida campos obligatorios, formato de email y existencia del rol asignado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campos vacíos o formato inválido"),
            @ApiResponse(responseCode = "404", description = "El rol especificado no existe"),
            @ApiResponse(responseCode = "409", description = "El email ya está en uso")
    })
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @Operation(summary = "US05 - Listar todos los usuarios", description = "Retorna la lista completa de usuarios con todos sus atributos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "US08 - Obtener usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioDTO> obtenerPorId(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @Operation(summary = "US06 - Editar información personal de un usuario", description = "Actualiza los datos del usuario. Valida formato y persistencia.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos con formato inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Email ya en uso")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizar(
            @Parameter(description = "ID del usuario a actualizar", example = "1") @PathVariable Integer id,
            @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @Operation(summary = "US07 - Eliminar cuenta de usuario", description = "Elimina al usuario de la base de datos garantizando la integridad de los datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminación exitosa"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario a eliminar", example = "1") @PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Usuario más activo", description = "Devuelve el ranking de usuarios según la cantidad de reportes que han registrado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking de usuarios obtenido correctamente")
    })
    @GetMapping("/top-reportadores")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<com.noistop.noistop.dtos.UsuarioReporteCountDTO>> obtenerTopReportadores() {
        return ResponseEntity.ok(usuarioService.obtenerTopReportadores());
    }
}
