package com.noistop.noistop.services;

import com.noistop.noistop.dtos.UbicacionDTO;
import com.noistop.noistop.entities.Ubicacion;
import com.noistop.noistop.exceptions.ConflictException;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.ReporteRepository;
import com.noistop.noistop.repositories.UbicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final ReporteRepository reporteRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository, ReporteRepository reporteRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.reporteRepository = reporteRepository;
    }

    // US09 - Registrar ubicación
    @Transactional
    public UbicacionDTO crear(UbicacionDTO dto) {
        Ubicacion ubicacion = dto.toEntity();
        return UbicacionDTO.fromEntity(ubicacionRepository.save(ubicacion));
    }

    // US10 - Listar ubicaciones
    @Transactional(readOnly = true)
    public List<UbicacionDTO> listar() {
        return ubicacionRepository.findAll().stream()
                .map(UbicacionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // US11 - Actualizar ubicación
    @Transactional
    public UbicacionDTO actualizar(Integer id, UbicacionDTO dto) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con id: " + id));

        if (dto.getUbicacion() != null) ubicacion.setUbicacion(dto.getUbicacion());
        if (dto.getDistrito() != null) ubicacion.setDistrito(dto.getDistrito());
        if (dto.getLongitud() != null) ubicacion.setLongitud(dto.getLongitud());
        if (dto.getLatitud() != null) ubicacion.setLatitud(dto.getLatitud());

        return UbicacionDTO.fromEntity(ubicacionRepository.save(ubicacion));
    }

    // US12 - Eliminar ubicación
    @Transactional
    public void eliminar(Integer id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con id: " + id));

        if (!ubicacion.getReportes().isEmpty()) {
            throw new ConflictException("No se puede eliminar la ubicación porque tiene reportes asociados");
        }

        ubicacionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UbicacionDTO obtenerPorId(Integer id) {
        return UbicacionDTO.fromEntity(
                ubicacionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con id: " + id))
        );
    }

    @Transactional(readOnly = true)
    public List<com.noistop.noistop.dtos.DistritoReporteDTO> obtenerTopDistritosConReportes() {
        return ubicacionRepository.findDistritosConMasReportes();
    }
}
