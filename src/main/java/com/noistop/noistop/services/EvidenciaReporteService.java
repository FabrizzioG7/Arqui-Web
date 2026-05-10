package com.noistop.noistop.services;

import com.noistop.noistop.dtos.EvidenciaReporteDTO;
import com.noistop.noistop.entities.EvidenciaReporte;
import com.noistop.noistop.entities.Reporte;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.EvidenciaReporteRepository;
import com.noistop.noistop.repositories.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvidenciaReporteService {

    private final EvidenciaReporteRepository evidenciaRepository;
    private final ReporteRepository reporteRepository;

    public EvidenciaReporteService(EvidenciaReporteRepository evidenciaRepository,
                                   ReporteRepository reporteRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.reporteRepository = reporteRepository;
    }

    // US19 - Registrar evidencia
    @Transactional
    public EvidenciaReporteDTO crear(EvidenciaReporteDTO dto) {
        if (!reporteRepository.existsById(dto.getReporteId())) {
            throw new ResourceNotFoundException("No existe un reporte con id: " + dto.getReporteId() + ". Debe existir al menos un registro en la tabla Reportes.");
        }
        Reporte reporte = reporteRepository.findById(dto.getReporteId()).get();
        EvidenciaReporte evidencia = dto.toEntity();
        evidencia.setReporte(reporte);
        return EvidenciaReporteDTO.fromEntity(evidenciaRepository.save(evidencia));
    }

    @Transactional(readOnly = true)
    public List<EvidenciaReporteDTO> listarPorReporte(Integer reporteId) {
        if (!reporteRepository.existsById(reporteId)) {
            throw new ResourceNotFoundException("Reporte no encontrado con id: " + reporteId);
        }
        return evidenciaRepository.findByReportePkReporteId(reporteId)
                .stream().map(EvidenciaReporteDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!evidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evidencia no encontrada con id: " + id);
        }
        evidenciaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EvidenciaReporteDTO> listar() {
        return evidenciaRepository.findAll().stream()
                .map(EvidenciaReporteDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
