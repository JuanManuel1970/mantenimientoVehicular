package com.lubricentro.mantenimiento.security;

import com.lubricentro.mantenimiento.model.Usuario;
import com.lubricentro.mantenimiento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(input)
                .or(() -> usuarioRepository.findByNombreUsuario(input))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + input));

        if (!usuario.isActivo()) {
            throw new UsuarioInactivoException("Tu cuenta está inactiva. Contactá al administrador.");
        }

        return new UsuarioDetails(usuario);
    }

}
