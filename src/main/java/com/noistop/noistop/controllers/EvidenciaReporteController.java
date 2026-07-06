package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.EvidenciaReporteDTO;
import com.noistop.noistop.services.EvidenciaReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/evidencias")
@Tag(name = "Evidencias de Reporte", description = "US19: Gestión de evidencias multimedia de reportes")
public class EvidenciaReporteController {

    private final EvidenciaReporteService evidenciaService;

    public EvidenciaReporteController(EvidenciaReporteService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @Operation(
            summary = "US19 - Registrar evidencia de un reporte",
            description = "Sube una imagen (multipart/form-data) y la asocia a un reporte existente. " +
                    "Formatos permitidos: jpg, jpeg, png, gif, webp. Máximo 5MB."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evidencia registrada"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido, formato no permitido o campos vacíos"),
            @ApiResponse(responseCode = "404", description = "No existe el reporte asociado")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','AUTHORITY')")
    public ResponseEntity<EvidenciaReporteDTO> crear(
            @Parameter(description = "Imagen de evidencia") @RequestParam("file") MultipartFile file,
            @Parameter(description = "ID del reporte asociado", example = "1") @RequestParam("reporteId") Integer reporteId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenciaService.crear(file, reporteId));
    }

    @Operation(summary = "Listar todas las evidencias")
    @GetMapping("/listar")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<List<EvidenciaReporteDTO>> listar() {
        return ResponseEntity.ok(evidenciaService.listar());
    }

    @Operation(summary = "Listar evidencias de un reporte específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de evidencias del reporte"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/reporte/{reporteId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<List<EvidenciaReporteDTO>> listarPorReporte(
            @Parameter(description = "ID del reporte", example = "1") @PathVariable Integer reporteId) {
        return ResponseEntity.ok(evidenciaService.listarPorReporte(reporteId));
    }

    @Operation(summary = "Mis Evidencias: listar todas las evidencias de los reportes de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<List<EvidenciaReporteDTO>> listarPorUsuario(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Integer usuarioId) {
        return ResponseEntity.ok(evidenciaService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Ver / descargar la imagen de una evidencia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen de la evidencia"),
            @ApiResponse(responseCode = "404", description = "Evidencia o archivo no encontrado")
    })
    @GetMapping("/{id}/archivo")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY','USER')")
    public ResponseEntity<Resource> obtenerArchivo(
            @Parameter(description = "ID de la evidencia", example = "1") @PathVariable Integer id) {
        Resource archivo = evidenciaService.obtenerArchivo(id);
        String contentType = detectarContentType(archivo.getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                .body(archivo);
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

    private String detectarContentType(String nombreArchivo) {
        if (nombreArchivo == null) return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        String ext = nombreArchivo.toLowerCase(Locale.ROOT);
        if (ext.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        if (ext.endsWith(".gif")) return MediaType.IMAGE_GIF_VALUE;
        if (ext.endsWith(".jpg") || ext.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        if (ext.endsWith(".webp")) return "image/webp";
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
