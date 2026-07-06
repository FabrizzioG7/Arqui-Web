package com.noistop.noistop.security;

import com.noistop.noistop.entities.Usuario;
import com.noistop.noistop.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Spring Security llama a este método con el valor que recibió en el campo "identificador" del login.
     * En NoiStop el usuario puede identificarse con su NOMBRE DE USUARIO (campo "nombre") o con su EMAIL.
     */
    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailOrNombre(identificador)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con usuario/email: " + identificador));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(usuario.getRol().getNombreRol()));

        // Se usa el email como "username" interno/subject del token, sin importar
        // con qué campo se haya identificado el usuario al hacer login.
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                roles
        );
    }

    /**
     * Devuelve la entidad Usuario completa a partir del identificador (nombre o email).
     * Se usa luego del login para armar la respuesta con nombre, email y rol.
     */
    public Usuario obtenerUsuario(String identificador) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailOrNombre(identificador)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con usuario/email: " + identificador));
    }
}
