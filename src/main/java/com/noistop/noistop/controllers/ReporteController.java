package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.ReporteDTO;
import com.noistop.noistop.services.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "US13-US20, US32-US33: Gestión de reportes de ruido")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "US15 - Registrar un reporte de ruido", description = "Crea un reporte con estado inicial 'pendiente'. Valida campos obligatorios y existencia de usuario, categoría y ubicación.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campos obligatorios faltantes"),
            @ApiResponse(responseCode = "404", description = "Usuario, categoría o ubicación no encontrados")
    })
    @PostMapping("/insertar")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','AUTHORITY')")
    public ResponseEntity<ReporteDTO> crear(@Valid @RequestBody ReporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crear(dto));
    }

    @Operation(summary = "US16 - Listar todos los reportes", description = "Retorna todos los reportes. Si no hay reportes retorna 'No existen reportes registrados'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reportes"),
            @ApiResponse(responseCode = "404", description = "No existen reportes registrados")
    })
    @GetMapping("/listar")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<List<ReporteDTO>> listar() {
        return ResponseEntity.ok(reporteService.listar());
    }

    @Operation(summary = "Obtener reporte por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<ReporteDTO> obtenerPorId(
            @Parameter(description = "ID del reporte", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }

    @Operation(summary = "US17 - Editar reporte", description = "Actualiza datos del reporte. Valida existencia del ID. Registra cambio de estado en historial.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontró el registro")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<ReporteDTO> actualizar(
            @Parameter(description = "ID del reporte", example = "1") @PathVariable Integer id,
            @RequestBody ReporteDTO dto) {
        return ResponseEntity.ok(reporteService.actualizar(id, dto));
    }

    @Operation(summary = "US18 - Eliminar un reporte")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reporte eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del reporte a eliminar", example = "1") @PathVariable Integer id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "US13 - Reportes por distrito", description = "Retorna reportes filtrados por nombre de distrito con metadatos (nombre, cantidad, fecha).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reportes del distrito"),
            @ApiResponse(responseCode = "400", description = "Parámetro 'distrito' obligatorio no enviado")
    })
    @GetMapping("/por-distrito")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<Map<String, Object>> reportesPorDistrito(
            @Parameter(description = "Nombre del distrito", example = "Miraflores")
            @RequestParam String distrito) {
        return ResponseEntity.ok(reporteService.reportesPorDistrito(distrito));
    }

    @Operation(summary = "US20 - Reportes agrupados por ubicación/distrito", description = "Muestra incidencias por ubicación con totales para identificar zonas con más y menos incidencias.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de distritos con cantidad de reportes"),
            @ApiResponse(responseCode = "404", description = "No existen reportes")
    })
    @GetMapping("/por-ubicacion")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<List<Map<String, Object>>> reportesPorUbicacion() {
        return ResponseEntity.ok(reporteService.reportesPorUbicacion());
    }

    @Operation(summary = "US32 - Historial de reportes de un usuario", description = "Lista todos los reportes del usuario con fecha, ubicación, categoría y estado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de reportes"),
            @ApiResponse(responseCode = "404", description = "No existen reportes registrados para este usuario")
    })
    @GetMapping("/historial/usuario/{usuarioId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<List<ReporteDTO>> historialPorUsuario(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Integer usuarioId) {
        return ResponseEntity.ok(reporteService.historialPorUsuario(usuarioId));
    }

    @Operation(summary = "US33 - Filtrar historial de reportes por fecha", description = "Retorna reportes del usuario en un rango de fechas. Valida que las fechas sean correctas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reportes en el rango de fechas"),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas o rango incorrecto"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reportes en el rango de fechas")
    })
    @GetMapping("/historial/usuario/{usuarioId}/filtrar")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<List<ReporteDTO>> historialPorFecha(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Integer usuarioId,
            @Parameter(description = "Fecha de inicio (ISO formato: 2024-01-01T00:00:00)", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Fecha de fin (ISO formato: 2024-12-31T23:59:59)", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(reporteService.historialPorUsuarioYFecha(usuarioId, inicio, fin));
    }
}
