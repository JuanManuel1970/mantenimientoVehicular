package com.lubricentro.mantenimiento.service;

import com.lubricentro.mantenimiento.model.Usuario;
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
    private ServicioEmail servicioEmail; // lo dejé tal cual lo tenías

    // --- Helpers ---
    private String normalizarPatente(String patente) {
        return patente == null ? null : patente.trim().toUpperCase();
    }

    // --- Consultas ---
    public List<Vehiculo> obtenerTodosOrdenadosPorUsuario() {
        return vehiculoRepository.findAllByOrderByUsuarioNombreUsuarioAsc();
    }

    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    public List<Vehiculo> obtenerPorUsuario(Usuario usuario) {
        return vehiculoRepository.findByUsuario(usuario);
    }


    public List<Vehiculo> buscarPropiosPorPatente(Usuario u, String q) {
        return vehiculoRepository.findByUsuarioAndPatenteContainingIgnoreCase(u, q);
    }

    public List<Vehiculo> buscarTodosPorPatenteAdmin(String q) {
        return vehiculoRepository.buscarTodosPorPatenteAdmin(q);
    }

    // --- Alta ---
    @Transactional
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        // Usuario obligatorio
        if (vehiculo.getUsuario() == null || vehiculo.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Falta asociar el usuario del vehículo.");
        }

        // Normalizar patente
        String pat = normalizarPatente(vehiculo.getPatente());
        vehiculo.setPatente(pat);

        // Unicidad por (usuario, patente)
        boolean existe = vehiculoRepository
                .existsByUsuarioIdAndPatenteIgnoreCase(vehiculo.getUsuario().getId(), pat);
        if (existe) {
            throw new IllegalArgumentException("La patente ya está registrada para este usuario.");
        }

        // Validaciones de dominio
        if (vehiculo.getFechaUltimoCambio() != null &&
                vehiculo.getFechaUltimoCambio().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del último cambio no puede ser futura.");
        }
        if (vehiculo.getAnio() != null && (vehiculo.getAnio() < 1940 || vehiculo.getAnio() > 2060)) {
            throw new IllegalArgumentException("El año debe estar entre 1940 y 2060.");
        }

        // Calcular próximos servicios
        vehiculo.calcularProximoMantenimiento();

        // Guardar
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public Vehiculo actualizarVehiculo(Long id, Vehiculo nuevo) {
        Vehiculo actual = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado."));

        // Usar SIEMPRE el usuario ya asociado en BD
        Long usuarioId = (actual.getUsuario() != null) ? actual.getUsuario().getId() : null;
        if (usuarioId == null) {
            throw new IllegalArgumentException("Falta asociar el usuario del vehículo.");
        }

        // Normalizar patente y validar unicidad dentro del MISMO usuario (excluyendo este id)
        String pat = normalizarPatente(nuevo.getPatente());
        boolean existe = vehiculoRepository
                .existsByUsuarioIdAndPatenteIgnoreCaseAndIdNot(usuarioId, pat, id);
        if (existe) {
            throw new IllegalArgumentException("La patente ya está registrada para este usuario.");
        }

        // Copiar campos editables (NO tocar el usuario)
        actual.setNombreCliente(nuevo.getNombreCliente());
        actual.setPatente(pat);
        actual.setMarca(nuevo.getMarca());
        actual.setModelo(nuevo.getModelo());
        actual.setAnio(nuevo.getAnio());
        actual.setKilometros(nuevo.getKilometros());
        actual.setMarcaAceite(nuevo.getMarcaAceite());
        actual.setDuracionAceite(nuevo.getDuracionAceite());
        actual.setKmPorMes(nuevo.getKmPorMes());
        actual.setFiltroAireCambiado(nuevo.isFiltroAireCambiado());
        actual.setFiltroCombustibleCambiado(nuevo.isFiltroCombustibleCambiado());
        actual.setFiltroAceiteCambiado(nuevo.isFiltroAceiteCambiado());
        actual.setEmail(nuevo.getEmail());
        actual.setFechaUltimoCambio(nuevo.getFechaUltimoCambio());
        // ❌ NO hacer: actual.setUsuario(...)

        actual.calcularProximoMantenimiento();
        return vehiculoRepository.save(actual);
    }


    // --- Borrado ---
    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }

    public void eliminarPorId(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
