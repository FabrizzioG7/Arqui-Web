package com.noistop.noistop.services;

import com.noistop.noistop.dtos.RolDTO;
import com.noistop.noistop.entities.Rol;
import com.noistop.noistop.exceptions.ConflictException;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.RolRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // US01 - Registrar rol
    @Transactional
    public RolDTO crear(RolDTO dto) {
        if (rolRepository.existsByNombreRol(dto.getNombreRol())) {
            throw new ConflictException("Ya existe un rol con el nombre: " + dto.getNombreRol());
        }
        Rol rol = dto.toEntity();
        return RolDTO.fromEntity(rolRepository.save(rol));
    }

    // US02 - Editar rol
    @Transactional
    public RolDTO actualizar(Integer id, RolDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        if (!rol.getNombreRol().equals(dto.getNombreRol()) && rolRepository.existsByNombreRol(dto.getNombreRol())) {
            throw new ConflictException("Ya existe un rol con el nombre: " + dto.getNombreRol());
        }
        rol.setNombreRol(dto.getNombreRol());
        return RolDTO.fromEntity(rolRepository.save(rol));
    }

    // US03 - Listar roles
    @Transactional(readOnly = true)
    public List<RolDTO> listar() {
        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No existen registros");
        }
        return roles.stream().map(RolDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RolDTO obtenerPorId(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existen registros"));
        return RolDTO.fromEntity(rol);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }
}
