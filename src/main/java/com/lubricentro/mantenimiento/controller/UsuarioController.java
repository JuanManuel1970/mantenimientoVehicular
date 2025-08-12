package com.lubricentro.mantenimiento.controller;

import com.lubricentro.mantenimiento.model.Usuario;
import com.lubricentro.mantenimiento.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


import java.time.LocalDate;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // === Registro de nuevos usuarios (externo) ===
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "registro"; // Vuelve a la vista mostrando errores
        }

        try {
            usuario.setFechaAlta(LocalDate.now());
            usuarioService.registrar(usuario); // guarda y encripta
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }


    @GetMapping("/login")
    public String mostrarLogin() {
        return "admin/login";
    }

    // === Gestión de usuarios (solo admin) ===

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model,
                                 @RequestParam(required = false) String mensaje,
                                 @RequestParam(required = false) String advertencia,
                                 @RequestParam(required = false) Long usuarioId) {

        List<Usuario> usuarios = usuarioService.obtenerTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("advertencia", advertencia);
        model.addAttribute("usuarioId", usuarioId);

        return "admin/usuarios";
    }

    @PostMapping("/admin/usuarios/{id}/activar")
    public String activarUsuario(@PathVariable Long id) {
        usuarioService.activarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/admin/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/admin/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id,
                                  @RequestParam(required = false) boolean forzar,
                                  Model model) {

        if (forzar) {
            usuarioService.eliminarUsuarioYVehiculos(id);
            model.addAttribute("mensaje", "Usuario y sus vehículos eliminados correctamente.");
        } else {
            boolean eliminado = usuarioService.eliminarUsuario(id);
            if (eliminado) {
                model.addAttribute("mensaje", "Usuario eliminado correctamente.");
            } else {
                model.addAttribute("advertencia", "El usuario tiene vehículos asociados. ¿Deseás eliminarlo junto con sus vehículos?");
                model.addAttribute("usuarioId", id);
            }
        }

        List<Usuario> usuarios = usuarioService.obtenerTodos();
        model.addAttribute("usuarios", usuarios);

        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuario_nuevo";
    }

    @PostMapping("/admin/usuarios")
    public String crearUsuarioAdmin(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuario.setFechaAlta(LocalDate.now());
            usuarioService.registrarNuevoUsuario(usuario);
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin/usuario_nuevo";
        }
    }



}
