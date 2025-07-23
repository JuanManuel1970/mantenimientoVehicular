package com.lubricentro.mantenimiento.repository;

import com.lubricentro.mantenimiento.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}
