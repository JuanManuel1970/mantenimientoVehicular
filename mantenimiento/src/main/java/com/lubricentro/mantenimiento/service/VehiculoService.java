package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    @Transactional
    public Vehiculo guardar(Vehiculo vehiculo) {
        vehiculo.calcularProximoServicio(); // asegura el cálculo
        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        // Enviar email
        enviarCorreoMantenimiento(guardado);

        return guardado;
    }

    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }

    private void enviarCorreoMantenimiento(Vehiculo vehiculo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(vehiculo.getEmail());
            mensaje.setSubject("📅 Recordatorio de próximo mantenimiento");
            mensaje.setText("Hola! Tu vehículo " + vehiculo.getMarca() + " " + vehiculo.getModelo() +
                    " con patente " + vehiculo.getPatente() +
                    " deberá realizar su próximo mantenimiento en: " +
                    vehiculo.getProximoServicioKm() + " km.");

            mailSender.send(mensaje);
        } catch (Exception e) {
            System.out.println("⚠️ Error al enviar correo: " + e.getMessage());
        }
    }
}
