package com.noistop.noistop.services;

import com.noistop.noistop.dtos.MedicionDTO;
import com.noistop.noistop.entities.Medicion;
import com.noistop.noistop.exceptions.ConflictException;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.MedicionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicionService {

    private final MedicionRepository medicionRepository;

    public MedicionService(MedicionRepository medicionRepository) {
        this.medicionRepository = medicionRepository;
    }

    // Registrar medición
    @Transactional
    public MedicionDTO crear(MedicionDTO dto) {
        Medicion medicion = dto.toEntity();
        return MedicionDTO.fromEntity(medicionRepository.save(medicion));
    }

    // Listar todas las mediciones
    @Transactional(readOnly = true)
    public List<MedicionDTO> listar() {
        List<Medicion> mediciones = medicionRepository.findAll();
        if (mediciones.isEmpty()) {
            throw new ResourceNotFoundException("No existen registros");
        }
        return mediciones.stream()
                .map(MedicionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Obtener medición por ID
    @Transactional(readOnly = true)
    public MedicionDTO obtenerPorId(Integer id) {
        return MedicionDTO.fromEntity(
                medicionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No existen registros"))
        );
    }

    // Actualizar medición
    @Transactional
    public MedicionDTO actualizar(Integer id, MedicionDTO dto) {
        Medicion medicion = medicionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medición no encontrada con id: " + id));

        if (dto.getDecibeles() != null) medicion.setDecibeles(dto.getDecibeles());
        if (dto.getFechaHora() != null) medicion.setFechaHora(dto.getFechaHora());

        return MedicionDTO.fromEntity(medicionRepository.save(medicion));
    }

    // Eliminar medición
    @Transactional
    public void eliminar(Integer id) {
        Medicion medicion = medicionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medición no encontrada con id: " + id));

        if (!medicion.getReportes().isEmpty()) {
            throw new ConflictException("No se puede eliminar la medición porque tiene reportes asociados");
        }

        medicionRepository.deleteById(id);
    }
}
