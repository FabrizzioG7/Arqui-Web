package com.noistop.noistop.services;

import com.noistop.noistop.dtos.ReporteDTO;
import com.noistop.noistop.entities.*;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRuidoRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final MedicionRepository medicionRepository;
    private final HistorialReporteRepository historialRepository;

    public ReporteService(ReporteRepository reporteRepository,
                          UsuarioRepository usuarioRepository,
                          CategoriaRuidoRepository categoriaRepository,
                          UbicacionRepository ubicacionRepository,
                          MedicionRepository medicionRepository,
                          HistorialReporteRepository historialRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.medicionRepository = medicionRepository;
        this.historialRepository = historialRepository;
    }

    // US15 - Registrar reporte
    @Transactional
    public ReporteDTO crear(ReporteDTO dto) {
        if (!usuarioRepository.existsById(dto.getUsuarioId())) {
            throw new ResourceNotFoundException("No existe al menos un usuario en el sistema con id: " + dto.getUsuarioId());
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.getUsuarioId()));

        CategoriaRuido categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + dto.getCategoriaId()));

        Ubicacion ubicacion = ubicacionRepository.findById(dto.getUbicacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada: " + dto.getUbicacionId()));

        Reporte reporte = new Reporte();
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setEstado("pendiente");
        reporte.setUsuario(usuario);
        reporte.setCategoriaRuido(categoria);
        reporte.setUbicacion(ubicacion);

        if (dto.getMedicionId() != null) {
            Medicion medicion = medicionRepository.findById(dto.getMedicionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medición no encontrada: " + dto.getMedicionId()));
            reporte.setMedicion(medicion);
        }

        return ReporteDTO.fromEntity(reporteRepository.save(reporte));
    }

    // US16 - Listar todos los reportes
    @Transactional(readOnly = true)
    public List<ReporteDTO> listar() {
        List<Reporte> reportes = reporteRepository.findAll();
        if (reportes.isEmpty()) {
            throw new ResourceNotFoundException("No existen reportes registrados");
        }
        return reportes.stream().map(ReporteDTO::fromEntity).collect(Collectors.toList());
    }

    // US17 - Editar reporte
    @Transactional
    public ReporteDTO actualizar(Integer id, ReporteDTO dto) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el registro con id: " + id));

        String estadoAnterior = reporte.getEstado();

        if (dto.getDescripcion() != null && !dto.getDescripcion().isBlank()) {
            reporte.setDescripcion(dto.getDescripcion());
        }
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            reporte.setEstado(dto.getEstado());
        }
        if (dto.getCategoriaId() != null) {
            CategoriaRuido categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + dto.getCategoriaId()));
            reporte.setCategoriaRuido(categoria);
        }
        if (dto.getUbicacionId() != null) {
            Ubicacion ubicacion = ubicacionRepository.findById(dto.getUbicacionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada: " + dto.getUbicacionId()));
            reporte.setUbicacion(ubicacion);
        }

        Reporte guardado = reporteRepository.save(reporte);

        // Registrar historial si cambió el estado
        if (!estadoAnterior.equals(guardado.getEstado())) {
            HistorialReporte historial = new HistorialReporte();
            historial.setReporte(guardado);
            historial.setEstadoAnterior(estadoAnterior);
            historial.setEstadoNuevo(guardado.getEstado());
            historial.setComentario("Cambio de estado desde API");
            historialRepository.save(historial);
        }

        return ReporteDTO.fromEntity(guardado);
    }

    // US18 - Eliminar reporte
    @Transactional
    public void eliminar(Integer id) {
        if (!reporteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
    }

    // US13 - Reportes por distrito
    @Transactional(readOnly = true)
    public Map<String, Object> reportesPorDistrito(String distrito) {
        if (distrito == null || distrito.isBlank()) {
            throw new IllegalArgumentException("El parámetro 'distrito' es obligatorio");
        }
        List<Reporte> reportes = reporteRepository.findByUbicacionDistritoContainingIgnoreCase(distrito);
        List<ReporteDTO> dtos = reportes.stream().map(ReporteDTO::fromEntity).collect(Collectors.toList());

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("distrito", distrito);
        resultado.put("cantidadReportes", dtos.size());
        resultado.put("fechaConsulta", LocalDateTime.now());
        resultado.put("reportes", dtos);
        return resultado;
    }

    // US20 - Reportes por ubicación con estadísticas
    @Transactional(readOnly = true)
    public List<Map<String, Object>> reportesPorUbicacion() {
        List<Object[]> resultados = reporteRepository.countReportesPorDistrito();
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException("No existen reportes");
        }
        return resultados.stream().map(r -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("distrito", r[0]);
            m.put("totalIncidencias", r[1]);
            return m;
        }).collect(Collectors.toList());
    }

    // US32 - Historial de reportes por usuario
    @Transactional(readOnly = true)
    public List<ReporteDTO> historialPorUsuario(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        List<Reporte> reportes = reporteRepository.findByUsuarioPkUsuarioId(usuarioId);
        if (reportes.isEmpty()) {
            throw new ResourceNotFoundException("No existen reportes registrados para este usuario");
        }
        return reportes.stream().map(ReporteDTO::fromEntity).collect(Collectors.toList());
    }

    // US33 - Filtrar historial por fecha
    @Transactional(readOnly = true)
    public List<ReporteDTO> historialPorUsuarioYFecha(Integer usuarioId, LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        List<Reporte> reportes = reporteRepository.findByUsuarioIdAndFechaRango(usuarioId, inicio, fin);
        if (reportes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron reportes en el rango de fechas indicado");
        }
        return reportes.stream().map(ReporteDTO::fromEntity).collect(Collectors.toList());
    }

    // Obtener reporte por ID
    @Transactional(readOnly = true)
    public ReporteDTO obtenerPorId(Integer id) {
        return ReporteDTO.fromEntity(
                reporteRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte con id: " + id))
        );
    }
}
