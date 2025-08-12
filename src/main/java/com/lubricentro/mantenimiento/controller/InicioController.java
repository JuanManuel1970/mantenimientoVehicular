package com.lubricentro.mantenimiento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {


    @GetMapping("/")
    public String redirigirInicio() {
        System.out.println("👉 Entrando a /");
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String mostrarInicio() {
        System.out.println("👉 Mostrando /inicio");
        return "inicio";
    }





}
