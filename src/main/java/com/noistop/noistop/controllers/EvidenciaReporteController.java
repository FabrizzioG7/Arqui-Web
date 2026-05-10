package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.EvidenciaReporteDTO;
import com.noistop.noistop.services.EvidenciaReporteService;
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
@RequestMapping("/api/evidencias")
@Tag(name = "Evidencias de Reporte", description = "US19: Gestión de evidencias multimedia de reportes")
public class EvidenciaReporteController {

    private final EvidenciaReporteService evidenciaService;

    public EvidenciaReporteController(EvidenciaReporteService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @Operation(summary = "US19 - Registrar evidencia de un reporte", description = "Guarda evidencia asociada a un reporte. Valida campos requeridos y existencia del reporte.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evidencia registrada"),
            @ApiResponse(responseCode = "400", description = "Campos requeridos vacíos"),
            @ApiResponse(responseCode = "404", description = "No existe el reporte asociado")
    })
    @PostMapping
    public ResponseEntity<EvidenciaReporteDTO> crear(@Valid @RequestBody EvidenciaReporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenciaService.crear(dto));
    }

    @Operation(summary = "Listar todas las evidencias")
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EvidenciaReporteDTO>> listar() {
        return ResponseEntity.ok(evidenciaService.listar());
    }

    @Operation(summary = "Listar evidencias de un reporte específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de evidencias del reporte"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EvidenciaReporteDTO>> listarPorReporte(
            @Parameter(description = "ID del reporte", example = "1") @PathVariable Integer reporteId) {
        return ResponseEntity.ok(evidenciaService.listarPorReporte(reporteId));
    }

    @Operation(summary = "Eliminar evidencia")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evidencia eliminada"),
            @ApiResponse(responseCode = "404", description = "Evidencia no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la evidencia", example = "1") @PathVariable Integer id) {
        evidenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
