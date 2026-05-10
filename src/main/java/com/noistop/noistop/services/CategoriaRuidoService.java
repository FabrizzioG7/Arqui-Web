package com.noistop.noistop.services;

import com.noistop.noistop.dtos.CategoriaRuidoDTO;
import com.noistop.noistop.dtos.ReporteDTO;
import com.noistop.noistop.entities.CategoriaRuido;
import com.noistop.noistop.exceptions.ConflictException;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.CategoriaRuidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaRuidoService {

    private final CategoriaRuidoRepository categoriaRepository;

    public CategoriaRuidoService(CategoriaRuidoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // US31 - Crear categoría
    @Transactional
    public CategoriaRuidoDTO crear(CategoriaRuidoDTO dto) {
        if (categoriaRepository.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.getNombreCategoria());
        }
        CategoriaRuido categoria = dto.toEntity();
        return CategoriaRuidoDTO.fromEntity(categoriaRepository.save(categoria));
    }

    // US25 - Listar categorías
    @Transactional(readOnly = true)
    public List<CategoriaRuidoDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaRuidoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // US28 - Obtener categoría por ID
    @Transactional(readOnly = true)
    public CategoriaRuidoDTO obtenerPorId(Integer id) {
        return CategoriaRuidoDTO.fromEntity(
                categoriaRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id))
        );
    }

    // US30 - Editar categoría
    @Transactional
    public CategoriaRuidoDTO actualizar(Integer id, CategoriaRuidoDTO dto) {
        CategoriaRuido categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        if (dto.getNombreCategoria() == null || dto.getNombreCategoria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        if (!categoria.getNombreCategoria().equals(dto.getNombreCategoria())
                && categoriaRepository.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.getNombreCategoria());
        }

        categoria.setNombreCategoria(dto.getNombreCategoria());
        if (dto.getDescripcionCategoria() != null) {
            categoria.setDescripcionCategoria(dto.getDescripcionCategoria());
        }

        return CategoriaRuidoDTO.fromEntity(categoriaRepository.save(categoria));
    }

    // US29 - Eliminar categoría
    @Transactional
    public void eliminar(Integer id) {
        CategoriaRuido categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        if (!categoria.getReportes().isEmpty()) {
            throw new ConflictException("No se puede eliminar la categoría porque tiene reportes asociados (FK_CategoriaID activo)");
        }

        categoriaRepository.deleteById(id);
    }

    // US27 - Buscar por nombre parcial
    @Transactional(readOnly = true)
    public List<CategoriaRuidoDTO> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreCategoriaContainingIgnoreCase(nombre)
                .stream().map(CategoriaRuidoDTO::fromEntity).collect(Collectors.toList());
    }

    // US26 - Listar reportes de una categoría
    @Transactional(readOnly = true)
    public List<ReporteDTO> listarReportesPorCategoria(Integer id) {
        CategoriaRuido categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        return categoria.getReportes().stream()
                .map(ReporteDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
