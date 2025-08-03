package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ServicioEmail {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoMantenimiento(Vehiculo vehiculo) {
        if (vehiculo.getEmail() == null || vehiculo.getEmail().isBlank()) {
            System.out.println("⚠️ Correo no enviado: email vacío.");
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(vehiculo.getEmail());
            mensaje.setSubject("📅 Mantenimiento registrado de tu vehículo");

            String texto = "¡Hola " + vehiculo.getNombreCliente() + "!\n\n" +
                    "Ya cargamos el mantenimiento de tu vehiculo " + vehiculo.getMarca() + " " + vehiculo.getModelo() +
                    " (patente: " + vehiculo.getPatente() + ").\n\n" +

                    "Último mantenimiento: " + vehiculo.getFechaUltimoCambio().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\n\n" +
                    "🛠 Cambios realizados:\n" +
                    "  - Filtro de aire " + (vehiculo.isFiltroAireCambiado() ? "✔" : "❌") + "\n" +
                    "  - Filtro de combustible " + (vehiculo.isFiltroCombustibleCambiado() ? "✔" : "❌") + "\n" +
                    "  - Filtro de aceite " + (vehiculo.isFiltroAceiteCambiado() ? "✔" : "❌") + "\n\n" +

                    "🔜 Te toca volver el " + vehiculo.getFechaProximoCambio().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                    " o al llegar a los " + vehiculo.getProximoServicioKm() + " km.\n\n" +

                    "☑️ Tranquilo, te vamos a mandar un mail automático 10 días antes 😉\n\n" +
                    "¡Gracias por elegirnos!";

            mensaje.setText(texto);
            mailSender.send(mensaje);
            System.out.println("✅ Correo enviado a " + vehiculo.getEmail());

        } catch (Exception e) {
            System.out.println("⚠️ Error al enviar correo: " + e.getMessage());
        }
    }

}

