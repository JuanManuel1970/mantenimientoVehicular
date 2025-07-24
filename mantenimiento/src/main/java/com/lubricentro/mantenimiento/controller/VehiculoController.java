package com.lubricentro.mantenimiento.controller;

import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.lubricentro.mantenimiento.service.VehiculoService;
import com.lubricentro.mantenimiento.service.ServicioEmail;
import jakarta.annotation.PostConstruct;





import java.util.List;


@Controller
public class VehiculoController {

    @PostConstruct
    public void debugRutaPlantillas() {
        System.out.println(">>> Plantillas cargadas desde: " + getClass().getClassLoader().getResource("templates/vehiculos.html"));
    }


    @Autowired
    private VehiculoRepository vehiculoRepository;

    @GetMapping("/vehiculos")
    public String listarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoRepository.findAll();
        model.addAttribute("vehiculos", vehiculos);
        return "vehiculos";
    }

    @GetMapping("/vehiculos/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
        return "redirect:/vehiculos";
    }

    @GetMapping("/vehiculos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("vehiculo", vehiculo);
        return "nuevo_vehiculo"; // o formulario_vehiculo si usás vista separada
    }
    @GetMapping("/vehiculos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "nuevo_vehiculo";
    }


    @PostMapping("/vehiculos")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo); // Hibernate actualiza si el id existe
        return "redirect:/vehiculos";
    }
    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ServicioEmail servicioEmail;


    @PostMapping("/vehiculos/enviarCorreo/{id}")
    public String enviarCorreo(@PathVariable Long id) {
        Optional<Vehiculo> optVehiculo = vehiculoService.obtenerPorId(id);

        if (optVehiculo.isPresent()) {
            servicioEmail.enviarCorreoMantenimiento(optVehiculo.get());
        }

        return "redirect:/vehiculos";
    }






}
