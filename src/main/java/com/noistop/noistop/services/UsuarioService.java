package com.noistop.noistop.services;

import com.noistop.noistop.dtos.UsuarioDTO;
import com.noistop.noistop.dtos.UsuarioReporteCountDTO;
import com.noistop.noistop.entities.Rol;
import com.noistop.noistop.entities.Usuario;
import com.noistop.noistop.exceptions.ConflictException;
import com.noistop.noistop.exceptions.ResourceNotFoundException;
import com.noistop.noistop.repositories.RolRepository;
import com.noistop.noistop.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // US04 - Registrar usuario
    @Transactional
    public UsuarioDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un registro en la tabla Rol con id: " + dto.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // BCrypt
        usuario.setRol(rol);

        return UsuarioDTO.fromEntity(usuarioRepository.save(usuario));
    }

    // US05 - Listar usuarios
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException("No existen registros");
        }
        return usuarios.stream().map(UsuarioDTO::fromEntity).collect(Collectors.toList());
    }

    // US06 - Editar usuario
    @Transactional
    public UsuarioDTO actualizar(Integer id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRolId() != null) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + dto.getRolId()));
            usuario.setRol(rol);
        }

        return UsuarioDTO.fromEntity(usuarioRepository.save(usuario));
    }

    // US07 - Eliminar usuario
    @Transactional
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // US08 - Buscar por ID
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existen registros"));
        return UsuarioDTO.fromEntity(usuario);
    }

    // Usuario más activo - Top reportadores
    @Transactional(readOnly = true)
    public List<UsuarioReporteCountDTO> obtenerTopReportadores() {
        return usuarioRepository.findTopReportadores();
    }
}
