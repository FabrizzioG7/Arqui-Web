package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.CategoriaRuidoDTO;
import com.noistop.noistop.dtos.ReporteDTO;
import com.noistop.noistop.services.CategoriaRuidoService;
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
@RequestMapping("/api/categorias")
@Tag(name = "Categorías de Ruido", description = "US25-US31: Gestión de categorías de ruido (Listar, Detalle, Buscar, Reportes por categoría, Crear, Editar, Eliminar)")
public class CategoriaRuidoController {

    private final CategoriaRuidoService categoriaService;

    public CategoriaRuidoController(CategoriaRuidoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "US31 - Crear nueva categoría de ruido", description = "Registra una categoría con nombre único. Genera automáticamente Created_at y Updated_at.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Nombre vacío o inválido"),
            @ApiResponse(responseCode = "409", description = "El nombre de categoría ya existe")
    })
    @PostMapping("/insertar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoriaRuidoDTO> crear(@Valid @RequestBody CategoriaRuidoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(dto));
    }

    @Operation(summary = "US25 - Listar todas las categorías / US27 - Buscar por nombre",
            description = "Sin parámetros: lista todas las categorías. Con parámetro 'nombre': filtra por coincidencia parcial (no sensible a mayúsculas).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorías (puede estar vacía)"),
    })
    @GetMapping("/listar")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','AUTHORITY')")
    public ResponseEntity<List<CategoriaRuidoDTO>> listar(
            @Parameter(description = "Texto parcial para buscar categorías por nombre (US27)", example = "tráfico")
            @RequestParam(required = false) String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
        }
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(summary = "US28 - Obtener detalle de categoría por ID", description = "Retorna todos los campos de la categoría: id, nombre, descripción y fechas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','AUTHORITY')")
    public ResponseEntity<CategoriaRuidoDTO> obtenerPorId(
            @Parameter(description = "ID de la categoría", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @Operation(summary = "US30 - Editar categoría", description = "Modifica nombre o descripción. No permite nombre vacío ni duplicado. Actualiza automáticamente Updated_at.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada - devuelve datos actualizados"),
            @ApiResponse(responseCode = "400", description = "Nombre vacío"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "Nombre de categoría duplicado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoriaRuidoDTO> actualizar(
            @Parameter(description = "ID de la categoría", example = "1") @PathVariable Integer id,
            @Valid @RequestBody CategoriaRuidoDTO dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @Operation(summary = "US29 - Eliminar categoría", description = "Elimina categoría sin reportes asociados. Si tiene reportes activos retorna error 409. Sin dependencias retorna 204.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La categoría tiene reportes asociados (FK_CategoriaID activo)")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la categoría a eliminar", example = "1") @PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "US26 - Listar reportes de una categoría", description = "Retorna todos los reportes vinculados a la categoría con ReporteID, descripción, estado y ubicación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reportes de la categoría"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}/reportes")
    @PreAuthorize("hasAnyAuthority('ADMIN','AUTHORITY')")
    public ResponseEntity<List<ReporteDTO>> reportesPorCategoria(
            @Parameter(description = "ID de la categoría", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.listarReportesPorCategoria(id));
    }
}
