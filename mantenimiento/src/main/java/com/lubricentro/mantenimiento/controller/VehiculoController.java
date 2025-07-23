package com.lubricentro.mantenimiento.controller;

import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




import java.util.List;

@Controller
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping("/vehiculos")
    public String listarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerTodos();
        model.addAttribute("vehiculos", vehiculos);
        return "vehiculos";
    }

    @GetMapping("/vehiculos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "nuevo_vehiculo";
    }

    @PostMapping("/vehiculos/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo) {
        vehiculoService.guardar(vehiculo); // <- CORRECTO
        return "redirect:/vehiculos";
    }


    @GetMapping("/vehiculos/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return "redirect:/vehiculos";
    }

    @GetMapping("/vehiculos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + id));

        model.addAttribute("vehiculo", vehiculo);
        return "formulario_vehiculo"; // asegurate de tener este .html
    }
}
