package com.lubricentro.mantenimiento.repository;

import com.lubricentro.mantenimiento.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lubricentro.mantenimiento.model.Usuario;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPatente(String patente);

    boolean existsByEmail(String email);

    Vehiculo findByEmail(String email);

    Vehiculo findByPatente(String patente);

    List<Vehiculo> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario); // ✅ nueva línea

    List<Vehiculo> findAllByOrderByUsuarioNombreUsuarioAsc();




}

