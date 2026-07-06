package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.AccionAdministrativaDTO;
import com.noistop.noistop.services.AccionAdministrativaService;
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
@RequestMapping("/api/acciones")
@Tag(name = "Acciones Administrativas", description = "US21-US24: Gestión de comentarios/acciones sobre reportes (Registrar, Listar historial, Editar, Eliminar)")
public class AccionAdministrativaController {

    private final AccionAdministrativaService accionService;

    public AccionAdministrativaController(AccionAdministrativaService accionService) {
        this.accionService = accionService;
    }

    @Operation(summary = "US21 - Registrar comentario/acción sobre un reporte", description = "Registra una acción de autoridad sobre un reporte abierto. Guarda fecha automáticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Acción registrada con fecha del momento"),
            @ApiResponse(responseCode = "400", description = "Campos obligatorios vacíos"),
            @ApiResponse(responseCode = "404", description = "Reporte o usuario no encontrado")
    })
    @PostMapping("/insertar")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<AccionAdministrativaDTO> crear(@Valid @RequestBody AccionAdministrativaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accionService.crear(dto));
    }

    @Operation(summary = "Listar todas las acciones administrativas")
    @GetMapping("/listar")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<List<AccionAdministrativaDTO>> listar() {
        return ResponseEntity.ok(accionService.listar());
    }

    @Operation(summary = "US23 - Listar historial de acciones de un reporte", description = "Muestra todas las acciones/comentarios con fechas de un reporte específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de acciones con fechas"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<List<AccionAdministrativaDTO>> historialPorReporte(
            @Parameter(description = "ID del reporte", example = "1") @PathVariable Integer reporteId) {
        return ResponseEntity.ok(accionService.listarPorReporte(reporteId));
    }

    @Operation(summary = "US24 - Editar comentario/acción", description = "Modifica el detalle de una acción existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Acción actualizada"),
            @ApiResponse(responseCode = "404", description = "Acción no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<AccionAdministrativaDTO> actualizar(
            @Parameter(description = "ID de la acción", example = "1") @PathVariable Integer id,
            @RequestBody AccionAdministrativaDTO dto) {
        return ResponseEntity.ok(accionService.actualizar(id, dto));
    }

    @Operation(summary = "US22 - Eliminar comentario/acción", description = "Elimina una acción/comentario sobre un reporte.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Acción eliminada"),
            @ApiResponse(responseCode = "404", description = "Acción no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la acción a eliminar", example = "1") @PathVariable Integer id) {
        accionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar accion administrativa", description = "Actualiza datos de la accion. Valida existencia del ID. Registra cambio de estado en historial.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<AccionAdministrativaDTO> listarPorId(
            @PathVariable Integer id){

        return ResponseEntity.ok(accionService.listarPorId(id));
    }
}
