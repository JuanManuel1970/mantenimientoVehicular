package com.lubricentro.mantenimiento.controller;

import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.service.ServicioEmail;
import com.lubricentro.mantenimiento.service.VehiculoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ServicioEmail servicioEmail;

    @PostConstruct
    public void debugRutaPlantillas() {
        System.out.println(">>> Plantillas cargadas desde: " + getClass().getClassLoader().getResource("templates/vehiculos.html"));
    }

    // === LISTADO ===
    @GetMapping("/vehiculos")
    public String listarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerTodos();
        model.addAttribute("vehiculos", vehiculos);
        return "vehiculos";
    }

    // === NUEVO VEHÍCULO ===
    @GetMapping("/vehiculos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "nuevo_vehiculo";
    }

    @PostMapping("/vehiculos")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo, Model model) {
        try {
            vehiculoService.guardarVehiculo(vehiculo);
            return "redirect:/vehiculos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("vehiculo", vehiculo);
            return "nuevo_vehiculo";
        }
    }

    // === EDITAR VEHÍCULO ===
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
            vehiculoService.guardarVehiculo(vehiculo);
            return "redirect:/vehiculos";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("vehiculo", vehiculo);
            return "nuevo_vehiculo";
        }
    }

    // === ELIMINAR VEHÍCULO ===
    @GetMapping("/vehiculos/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarPorId(id);
        return "redirect:/vehiculos";
    }


    @PostMapping("/vehiculos/enviarCorreo/{id}")
    public String enviarCorreo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Vehiculo> optVehiculo = vehiculoService.obtenerPorId(id);

        if (optVehiculo.isPresent()) {
            servicioEmail.enviarCorreoMantenimiento(optVehiculo.get());
            redirectAttributes.addFlashAttribute("mensaje", "Correo enviado con éxito.");
        }

        return "redirect:/vehiculos";
    }



}
