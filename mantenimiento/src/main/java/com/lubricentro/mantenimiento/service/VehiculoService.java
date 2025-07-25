package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Vehiculo;
import com.lubricentro.mantenimiento.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        // Verifica si existe otro con la misma patente
        Vehiculo existentePorPatente = vehiculoRepository.findByPatente(vehiculo.getPatente());
        if (existentePorPatente != null && !existentePorPatente.getId().equals(vehiculo.getId())) {
            throw new IllegalArgumentException("La patente ya está registrada.");
        }

        // Verifica si existe otro con el mismo email
        Vehiculo existentePorEmail = vehiculoRepository.findByEmail(vehiculo.getEmail());
        if (existentePorEmail != null && !existentePorEmail.getId().equals(vehiculo.getId())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        // Fecha no futura
        if (vehiculo.getFechaUltimoCambio() != null && vehiculo.getFechaUltimoCambio().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del último cambio no puede ser futura.");
        }
        if (vehiculo.getAnio() != null && (vehiculo.getAnio() < 1940 || vehiculo.getAnio() > 2060)) {
            throw new IllegalArgumentException("El año debe estar entre 1940 y 2060.");
        }

        vehiculo.calcularProximoMantenimiento();

        return vehiculoRepository.save(vehiculo);
    }



    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }


    public void eliminarPorId(Long id) {
        vehiculoRepository.deleteById(id);
    }

}
