package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.RolDTO;
import com.noistop.noistop.services.RolService;
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
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "US01-US03: Gestión de roles del sistema (US01: Crear, US02: Editar, US03: Listar)")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @Operation(summary = "US01 - Registrar un nuevo rol", description = "Crea un nuevo rol. Valida campos obligatorios y unicidad del nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rol creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o campos vacíos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un rol con ese nombre")
    })
    @PostMapping("/insertar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RolDTO> crear(@Valid @RequestBody RolDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(dto));
    }

    @Operation(summary = "US03 - Listar todos los roles", description = "Retorna todos los roles registrados. Si no hay registros retorna mensaje de error.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de roles"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RolDTO>> listar() {
        return ResponseEntity.ok(rolService.listar());
    }

    @Operation(summary = "Listar roles (público)", description = "Retorna todos los roles registrados. No requiere estar autenticado; se usa para llenar el combo de la pantalla de registro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de roles"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping("/publico")
    public ResponseEntity<List<RolDTO>> listarPublico() {
        return ResponseEntity.ok(rolService.listar());
    }

    @Operation(summary = "Obtener rol por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerPorId(
            @Parameter(description = "ID del rol", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(rolService.obtenerPorId(id));
    }

    @Operation(summary = "US02 - Editar un rol existente", description = "Actualiza el nombre del rol. Valida formato y unicidad.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @ApiResponse(responseCode = "409", description = "Nombre de rol duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> actualizar(
            @Parameter(description = "ID del rol a actualizar", example = "1") @PathVariable Integer id,
            @Valid @RequestBody RolDTO dto) {
        return ResponseEntity.ok(rolService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un rol")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rol eliminado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del rol a eliminar", example = "1") @PathVariable Integer id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
