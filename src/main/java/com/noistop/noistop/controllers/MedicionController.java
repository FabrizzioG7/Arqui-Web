package com.noistop.noistop.controllers;

import com.noistop.noistop.dtos.MedicionDTO;
import com.noistop.noistop.services.MedicionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
@Tag(name = "Mediciones", description = "CRUD de mediciones de decibeles asociadas a reportes de ruido")
public class MedicionController {

    private final MedicionService medicionService;

    public MedicionController(MedicionService medicionService) {
        this.medicionService = medicionService;
    }

    @Operation(summary = "Registrar una nueva medición",
            description = "Crea una medición con nivel de decibeles y fecha/hora. Valida campos obligatorios y rango de decibeles (0-200).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medición creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Campos vacíos o decibeles fuera de rango")
    })
    @PostMapping
    public ResponseEntity<MedicionDTO> crear(@Valid @RequestBody MedicionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicionService.crear(dto));
    }

    @Operation(summary = "Listar todas las mediciones",
            description = "Retorna todas las mediciones registradas con id, decibeles, fechaHora y createdAt.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de mediciones"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping
    public ResponseEntity<List<MedicionDTO>> listar() {
        return ResponseEntity.ok(medicionService.listar());
    }

    @Operation(summary = "Obtener medición por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medición encontrada"),
            @ApiResponse(responseCode = "404", description = "No existen registros")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicionDTO> obtenerPorId(
            @Parameter(description = "ID de la medición", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(medicionService.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar medición",
            description = "Permite actualización parcial de campos. Valida existencia del registro y retorna HTTP 200 con objeto actualizado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medición actualizada - retorna objeto actualizado"),
            @ApiResponse(responseCode = "400", description = "Decibeles fuera de rango"),
            @ApiResponse(responseCode = "404", description = "Medición no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicionDTO> actualizar(
            @Parameter(description = "ID de la medición", example = "1") @PathVariable Integer id,
            @RequestBody MedicionDTO dto) {
        return ResponseEntity.ok(medicionService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar medición",
            description = "Elimina la medición. No permite eliminar si tiene reportes asociados. Retorna HTTP 204.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medición eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Medición no encontrada"),
            @ApiResponse(responseCode = "409", description = "La medición tiene reportes asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la medición", example = "1") @PathVariable Integer id) {
        medicionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
