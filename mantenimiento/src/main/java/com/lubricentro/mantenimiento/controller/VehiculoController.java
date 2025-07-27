package com.lubricentro.mantenimiento.controller;

import com.lubricentro.mantenimiento.model.Usuario;
import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.service.ServicioEmail;
import com.lubricentro.mantenimiento.service.UsuarioService;
import com.lubricentro.mantenimiento.service.VehiculoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicioEmail servicioEmail;

    @PostConstruct
    public void debugRutaPlantillas() {
        System.out.println(">>> Plantillas cargadas desde: " + getClass().getClassLoader().getResource("templates/vehiculos.html"));
    }

    @GetMapping("/vehiculos")
    public String listarVehiculos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) {
            return "redirect:/login?error";
        }

        if ("ADMIN".equals(usuario.getRol())) {
            List<Vehiculo> vehiculos = vehiculoService.obtenerTodosOrdenadosPorUsuario();

            Set<Usuario> usuariosUnicos = vehiculos.stream()
                    .map(Vehiculo::getUsuario)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            model.addAttribute("usuariosUnicos", usuariosUnicos);
        } else {
            List<Vehiculo> vehiculos = vehiculoService.obtenerPorUsuario(usuario);
            model.addAttribute("vehiculos", vehiculos);
        }

        model.addAttribute("nombreUsuario", usuario.getNombre());
        return "vehiculos";
    }

    @GetMapping("/vehiculos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setFechaUltimoCambio(LocalDate.now());
        model.addAttribute("vehiculo", vehiculo);
        return "nuevo_vehiculo";
    }

    @PostMapping("/vehiculos")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email);
            vehiculo.setUsuario(usuario);

            vehiculoService.guardarVehiculo(vehiculo);
            return "redirect:/vehiculos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("vehiculo", vehiculo);
            return "nuevo_vehiculo";
        }
    }

    @GetMapping("/vehiculos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("vehiculo", vehiculo);
        return "nuevo_vehiculo";
    }

    @PostMapping("/vehiculos/{id}")
    public String actualizarVehiculo(@PathVariable Long id, @ModelAttribute Vehiculo vehiculo, Model model) {
        vehiculo.setId(id);
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email);
            vehiculo.setUsuario(usuario);

            vehiculoService.guardarVehiculo(vehiculo);
            return "redirect:/vehiculos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("vehiculo", vehiculo);
            return "nuevo_vehiculo";
        }
    }

    @GetMapping("/vehiculos/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarPorId(id);
        return "redirect:/vehiculos";
    }

    @PostMapping("/vehiculos/enviarCorreo/{id}")
    public String enviarCorreo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Vehiculo> optVehiculo = vehiculoService.obtenerPorId(id);

        optVehiculo.ifPresent(vehiculo -> {
            servicioEmail.enviarCorreoMantenimiento(vehiculo);
            redirectAttributes.addFlashAttribute("mensaje", "Correo enviado con éxito.");
        });

        return "redirect:/vehiculos";
    }
}
