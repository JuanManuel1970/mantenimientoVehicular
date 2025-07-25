package com.lubricentro.mantenimiento.repository;

import com.lubricentro.mantenimiento.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPatente(String patente);

    boolean existsByEmail(String email);

    Vehiculo findByEmail(String email);

    Vehiculo findByPatente(String patente);
}
