package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Usuario;
import com.lubricentro.mantenimiento.repository.UsuarioRepository;
import com.lubricentro.mantenimiento.repository.VehiculoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear admin por defecto si no existe
    @PostConstruct
    public void crearAdminPorDefecto() {
        Optional<Usuario> adminExistente = usuarioRepository.findByEmail("admin@admin.com");

        if (adminExistente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRol("ADMIN");
            admin.setActivo(true);

            usuarioRepository.save(admin);
            System.out.println("🛡️ Usuario admin creado (admin@admin.com / admin)");
        } else {
            System.out.println("✔️ Usuario admin ya existe.");
        }
    }

    public void registrarNuevoUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email.");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("USER");
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }

    public void registrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        usuario.setActivo(true);
        usuario.setRol("USER");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public void activarUsuario(Long id) {
        cambiarEstado(id, true);
    }

    public void desactivarUsuario(Long id) {
        cambiarEstado(id, false);
    }

    private void cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no encontrado"));

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + email));
    }

    // ✅ Eliminación controlada: si tiene vehículos no se elimina
    public boolean eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no encontrado"));

        if (!vehiculoRepository.findByUsuario(usuario).isEmpty()) {
            return false; // ❌ Tiene vehículos, se avisa
        }

        usuarioRepository.deleteById(id); // ✅ No tiene vehículos, se elimina
        return true;
    }

    // ✅ Eliminación forzada: elimina vehículos y luego el usuario
    @Transactional
    public void eliminarUsuarioYVehiculos(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no encontrado"));

        vehiculoRepository.deleteByUsuario(usuario); // elimina todos los vehículos
        usuarioRepository.deleteById(id); // luego el usuario
    }
}
