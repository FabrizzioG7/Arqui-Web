package com.noistop.noistop.services;

import com.noistop.noistop.dtos.AccionAdministrativaDTO;
import com.noistop.noistop.entities.AccionAdministrativa;
import com.noistop.noistop.entities.Reporte;
import com.noistop.noistop.entities.Usuario;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.AccionAdministrativaRepository;
import com.noistop.noistop.repositories.ReporteRepository;
import com.noistop.noistop.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccionAdministrativaService {

    private final AccionAdministrativaRepository accionRepository;
    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public AccionAdministrativaService(AccionAdministrativaRepository accionRepository,
                                       ReporteRepository reporteRepository,
                                       UsuarioRepository usuarioRepository) {
        this.accionRepository = accionRepository;
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // US21 - Registrar comentario/acción sobre un reporte
    @Transactional
    public AccionAdministrativaDTO crear(AccionAdministrativaDTO dto) {
        Reporte reporte = reporteRepository.findById(dto.getReporteId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + dto.getReporteId()));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        AccionAdministrativa accion = new AccionAdministrativa();
        accion.setDetalle(dto.getDetalle());
        accion.setReporte(reporte);
        accion.setUsuario(usuario);

        return AccionAdministrativaDTO.fromEntity(accionRepository.save(accion));
    }

    // US23 - Listar acciones por reporte (historial)
    @Transactional(readOnly = true)
    public List<AccionAdministrativaDTO> listarPorReporte(Integer reporteId) {
        if (!reporteRepository.existsById(reporteId)) {
            throw new ResourceNotFoundException("Reporte no encontrado con id: " + reporteId);
        }
        return accionRepository.findByReportePkReporteId(reporteId)
                .stream().map(AccionAdministrativaDTO::fromEntity).collect(Collectors.toList());
    }

    // US24 - Editar comentario/acción
    @Transactional
    public AccionAdministrativaDTO actualizar(Integer id, AccionAdministrativaDTO dto) {
        AccionAdministrativa accion = accionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acción no encontrada con id: " + id));

        if (dto.getDetalle() != null && !dto.getDetalle().isBlank()) {
            accion.setDetalle(dto.getDetalle());
        }

        return AccionAdministrativaDTO.fromEntity(accionRepository.save(accion));
    }

    // US22 - Eliminar comentario/acción
    @Transactional
    public void eliminar(Integer id) {
        if (!accionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Acción no encontrada con id: " + id);
        }
        accionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AccionAdministrativaDTO> listar() {
        return accionRepository.findAll().stream()
                .map(AccionAdministrativaDTO::fromEntity).collect(Collectors.toList());
    }
}
