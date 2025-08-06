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
    public String mostrarFormularioNuevo(@RequestParam(required = false) String nombre,
                                         @RequestParam(required = false) String email,
                                         Model model) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setFechaUltimoCambio(LocalDate.now());

        if (nombre != null) vehiculo.setNombreCliente(nombre);
        if (email != null) vehiculo.setEmail(email);

        model.addAttribute("vehiculo", vehiculo);
        return "nuevo_vehiculo";
    }


    @PostMapping("/vehiculos")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo, Model model) {
        List<String> errores = new ArrayList<>();

        // Validaciones
        if (!esPatenteValida(vehiculo.getPatente())) {
            errores.add("La patente ingresada no es válida. Use formato ABC123 o AB123CD.");
        }
        if (vehiculo.getKilometros() <= 10 || vehiculo.getKilometros() >= 2_000_000) {
            errores.add("Los kilómetros actuales deben ser mayores a 10 y menores a 2.000.000.");
        }
        if (vehiculo.getDuracionAceite() <= 1000 || vehiculo.getDuracionAceite() >= 20000) {
            errores.add("La duración del aceite debe ser mayor a 1000 y menor a 20000.");
        }
        if (vehiculo.getKmPorMes() <= 150 || vehiculo.getKmPorMes() >= vehiculo.getDuracionAceite()) {
            errores.add("Los km/mes estimados deben ser mayores a 150 y menores que la duración del aceite.");
        }

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("vehiculo", vehiculo);
            return "nuevo_vehiculo";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);
        vehiculo.setUsuario(usuario);

        vehiculoService.guardarVehiculo(vehiculo);
        return "redirect:/vehiculos";
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
        List<String> errores = new ArrayList<>();

        if (!esPatenteValida(vehiculo.getPatente())) {
            errores.add("La patente ingresada no es válida. Use formato ABC123 o AB123CD.");
        }
        if (vehiculo.getKilometros() <= 10 || vehiculo.getKilometros() >= 2_000_000) {
            errores.add("Los kilómetros actuales deben ser mayores a 10 y menores a 2.000.000.");
        }
        if (vehiculo.getDuracionAceite() <= 1000 || vehiculo.getDuracionAceite() >= 20000) {
            errores.add("La duración del aceite debe ser mayor a 1000 y menor a 20000.");
        }
        if (vehiculo.getKmPorMes() <= 150 || vehiculo.getKmPorMes() >= vehiculo.getDuracionAceite()) {
            errores.add("Los km/mes estimados deben ser mayores a 150 y menores que la duración del aceite.");
        }

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("vehiculo", vehiculo);
            return "nuevo_vehiculo";
        }

        Vehiculo vehiculoExistente = vehiculoService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

        vehiculo.setUsuario(vehiculoExistente.getUsuario());
        vehiculo.setId(id);

        vehiculoService.guardarVehiculo(vehiculo);
        return "redirect:/vehiculos";
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

    private boolean esPatenteValida(String patente) {
        return patente != null && patente.toUpperCase().matches("^([A-Z]{2}\\d{3}[A-Z]{2}|[A-Z]{3}\\d{3})$");
    }

    @GetMapping("/vehiculos/buscar")
    public String buscarPorPatente(@RequestParam String patente, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) {
            return "redirect:/login?error";
        }

        // Normalizar patente
        String patenteBuscada = patente.trim().toUpperCase();

        // Filtrar según rol
        Optional<Vehiculo> vehiculoEncontrado;
        if ("ADMIN".equals(usuario.getRol())) {
            vehiculoEncontrado = vehiculoService.obtenerTodos().stream()
                    .filter(v -> v.getPatente().equalsIgnoreCase(patenteBuscada))
                    .findFirst();
        } else {
            vehiculoEncontrado = vehiculoService.obtenerPorUsuario(usuario).stream()
                    .filter(v -> v.getPatente().equalsIgnoreCase(patenteBuscada))
                    .findFirst();
        }

        if (vehiculoEncontrado.isPresent()) {
            // Mostrar solo el vehículo encontrado
            model.addAttribute("vehiculos", List.of(vehiculoEncontrado.get()));
        } else {
            model.addAttribute("mensaje", "No se encontró ningún vehículo con la patente: " + patenteBuscada);
            model.addAttribute("vehiculos", Collections.emptyList());
        }

        model.addAttribute("nombreUsuario", usuario.getNombre());
        return "vehiculos";
    }


}
