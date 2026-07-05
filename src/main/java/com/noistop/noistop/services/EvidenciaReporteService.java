package com.noistop.noistop.services;

import com.noistop.noistop.dtos.EvidenciaReporteDTO;
import com.noistop.noistop.entities.EvidenciaReporte;
import com.noistop.noistop.entities.Reporte;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.EvidenciaReporteRepository;
import com.noistop.noistop.repositories.ReporteRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvidenciaReporteService {

    private final EvidenciaReporteRepository evidenciaRepository;
    private final ReporteRepository reporteRepository;
    private final FileStorageService fileStorageService;

    public EvidenciaReporteService(EvidenciaReporteRepository evidenciaRepository,
                                   ReporteRepository reporteRepository,
                                   FileStorageService fileStorageService) {
        this.evidenciaRepository = evidenciaRepository;
        this.reporteRepository = reporteRepository;
        this.fileStorageService = fileStorageService;
    }

    // US19 - Registrar evidencia (sube la imagen real al servidor)
    @Transactional
    public EvidenciaReporteDTO crear(MultipartFile file, Integer reporteId) {
        if (reporteId == null || !reporteRepository.existsById(reporteId)) {
            throw new ResourceNotFoundException(
                    "No existe un reporte con id: " + reporteId + ". Debe existir al menos un registro en la tabla Reportes.");
        }
        Reporte reporte = reporteRepository.findById(reporteId).get();

        // Puede lanzar IllegalArgumentException (400) si el archivo no es válido
        String nombreArchivoGuardado = fileStorageService.guardar(file);

        EvidenciaReporte evidencia = new EvidenciaReporte();
        evidencia.setRutaArchivo(nombreArchivoGuardado);
        evidencia.setNombreOriginal(file.getOriginalFilename());
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

    // US19 - "Mis Evidencias": evidencias de todos los reportes de un usuario
    @Transactional(readOnly = true)
    public List<EvidenciaReporteDTO> listarPorUsuario(Integer usuarioId) {
        return evidenciaRepository.findByReporte_Usuario_PkUsuarioId(usuarioId)
                .stream().map(EvidenciaReporteDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Integer id) {
        EvidenciaReporte evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada con id: " + id));
        fileStorageService.eliminar(evidencia.getRutaArchivo());
        evidenciaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EvidenciaReporteDTO> listar() {
        return evidenciaRepository.findAll().stream()
                .map(EvidenciaReporteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Resource obtenerArchivo(Integer id) {
        EvidenciaReporte evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada con id: " + id));
        return fileStorageService.cargarComoResource(evidencia.getRutaArchivo());
    }
}
