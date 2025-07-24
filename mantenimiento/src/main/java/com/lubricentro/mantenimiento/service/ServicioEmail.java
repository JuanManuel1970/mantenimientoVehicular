package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
            mensaje.setSubject("📅 Recordatorio de próximo mantenimiento");
            mensaje.setText("Hola! Tu vehículo " + vehiculo.getMarca() + " " + vehiculo.getModelo() +
                    " con patente " + vehiculo.getPatente() +
                    " deberá realizar su próximo mantenimiento en: " +
                    vehiculo.getProximoServicioKm() + " km.");

            mailSender.send(mensaje);
            System.out.println("✅ Correo enviado a " + vehiculo.getEmail());

        } catch (Exception e) {
            System.out.println("⚠️ Error al enviar correo: " + e.getMessage());
        }
    }

}
