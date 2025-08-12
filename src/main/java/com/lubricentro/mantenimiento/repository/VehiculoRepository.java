package com.lubricentro.mantenimiento.repository;

import com.lubricentro.mantenimiento.model.Usuario;
import com.lubricentro.mantenimiento.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPatente(String patente);
    Vehiculo findByPatente(String patente);

    boolean existsByEmail(String email);
    Vehiculo findByEmail(String email);

    boolean existsByUsuarioIdAndPatenteIgnoreCase(Long usuarioId, String patente);
    boolean existsByUsuarioIdAndPatenteIgnoreCaseAndIdNot(Long usuarioId, String patente, Long id);

    List<Vehiculo> findByUsuario(Usuario usuario);
    void deleteByUsuario(Usuario usuario);

    List<Vehiculo> findAllByOrderByUsuarioNombreUsuarioAsc();

    // ✅ NUEVO: búsqueda para usuario común (dentro de sus vehículos)
    List<Vehiculo> findByUsuarioAndPatenteContainingIgnoreCase(Usuario usuario, String q);

    // ✅ NUEVO: búsqueda para ADMIN (en todos los usuarios)
    @Query("""
      select v
      from Vehiculo v
      join fetch v.usuario u
      where upper(v.patente) like upper(concat('%', :q, '%'))
      order by u.nombreUsuario asc, v.patente asc
    """)
    List<Vehiculo> buscarTodosPorPatenteAdmin(@Param("q") String q);
}
