package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ServicioEmail servicioEmail; // ✅ Se inyecta el servicio de email

    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    @Transactional
    public Vehiculo guardar(Vehiculo vehiculo) {
        vehiculo.calcularProximoMantenimiento(); // Asegura el cálculo antes de guardar
        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        // ✅ Enviar correo después de guardar
        servicioEmail.enviarCorreoMantenimiento(guardado);

        return guardado;
    }

    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }



}
