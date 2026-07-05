package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.UbicacionDTO;
import com.noistop.noistop.services.UbicacionService;
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
@RequestMapping("/api/ubicaciones")
@Tag(name = "Ubicaciones", description = "US09-US12: Gestión de ubicaciones geográficas (Crear, Listar, Actualizar, Eliminar)")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @Operation(summary = "US09 - Registrar una nueva ubicación", description = "Guarda ubicación con coordenadas geográficas. Valida latitud, longitud y campos requeridos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ubicación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Campos vacíos o coordenadas inválidas")
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<UbicacionDTO> crear(@Valid @RequestBody UbicacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionService.crear(dto));
    }

    @Operation(summary = "US10 - Listar todas las ubicaciones", description = "Retorna todas las ubicaciones con id, ubicación, latitud, longitud y fecha de creación.")
    @ApiResponse(responseCode = "200", description = "Lista de ubicaciones (puede estar vacía)")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','AUTHORITY')")
    public ResponseEntity<List<UbicacionDTO>> listar() {
        return ResponseEntity.ok(ubicacionService.listar());
    }

    @Operation(summary = "Obtener ubicación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ubicación encontrada"),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','AUTHORITY')")
    public ResponseEntity<UbicacionDTO> obtenerPorId(
            @Parameter(description = "ID de la ubicación", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(ubicacionService.obtenerPorId(id));
    }

    @Operation(summary = "US11 - Actualizar ubicación", description = "Permite actualización parcial de campos. Valida coordenadas y existencia del registro. Retorna HTTP 200 con objeto actualizado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ubicación actualizada - retorna objeto actualizado"),
            @ApiResponse(responseCode = "400", description = "Coordenadas fuera de rango"),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<UbicacionDTO> actualizar(
            @Parameter(description = "ID de la ubicación", example = "1") @PathVariable Integer id,
            @RequestBody UbicacionDTO dto) {
        return ResponseEntity.ok(ubicacionService.actualizar(id, dto));
    }

    @Operation(summary = "US12 - Eliminar ubicación", description = "Elimina ubicación. No permite eliminar si tiene reportes asociados. Retorna HTTP 204.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ubicación eliminada"),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada"),
            @ApiResponse(responseCode = "409", description = "La ubicación tiene reportes asociados")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la ubicación", example = "1") @PathVariable Integer id) {
        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener distritos con más reportes", description = "Retorna una lista de distritos ordenados descendentemente por la cantidad de reportes asociados.")
    @ApiResponse(responseCode = "200", description = "Lista de distritos y su cantidad de reportes")
    @GetMapping("/top-distritos")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<List<com.noistop.noistop.dtos.DistritoReporteDTO>> obtenerTopDistritosConReportes() {
        return ResponseEntity.ok(ubicacionService.obtenerTopDistritosConReportes());
    }
}
