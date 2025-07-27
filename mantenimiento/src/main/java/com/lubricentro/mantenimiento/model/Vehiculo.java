package com.lubricentro.mantenimiento.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCliente;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private Integer kilometros;
    private String marcaAceite;
    private Integer duracionAceite;
    private Integer kmPorMes;
    private boolean filtroAireCambiado;
    private boolean filtroCombustibleCambiado;
    private boolean filtroAceiteCambiado;
    private String email;

    private Integer proximoServicioKm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaUltimoCambio;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDate fechaProximoCambio;




    // === Lógica de mantenimiento ===
    public void calcularProximoMantenimiento() {
        if (kilometros != null && duracionAceite != null) {
            this.proximoServicioKm = kilometros + duracionAceite;
        } else {
            this.proximoServicioKm = null;
        }

        if (fechaUltimoCambio != null && kmPorMes != null && kmPorMes > 0 && duracionAceite != null) {
            double mesesDuracion = (double) duracionAceite / kmPorMes;
            int diasDuracion = (int) Math.floor(mesesDuracion * 30);
            this.fechaProximoCambio = fechaUltimoCambio.plusDays(diasDuracion);
        } else {
            this.fechaProximoCambio = null;
        }
    }

    // === Getters y Setters ===


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMarcaAceite() {
        return marcaAceite;
    }

    public void setMarcaAceite(String marcaAceite) {
        this.marcaAceite = marcaAceite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getKilometros() {
        return kilometros;
    }

    public void setKilometros(Integer kilometros) {
        this.kilometros = kilometros;
    }

    public Integer getDuracionAceite() {
        return duracionAceite;
    }

    public void setDuracionAceite(Integer duracionAceite) {
        this.duracionAceite = duracionAceite;
    }

    public Integer getKmPorMes() {
        return kmPorMes;
    }

    public void setKmPorMes(Integer kmPorMes) {
        this.kmPorMes = kmPorMes;
    }

    public boolean isFiltroAireCambiado() {
        return filtroAireCambiado;
    }

    public void setFiltroAireCambiado(boolean filtroAireCambiado) {
        this.filtroAireCambiado = filtroAireCambiado;
    }

    public boolean isFiltroCombustibleCambiado() {
        return filtroCombustibleCambiado;
    }

    public void setFiltroCombustibleCambiado(boolean filtroCombustibleCambiado) {
        this.filtroCombustibleCambiado = filtroCombustibleCambiado;
    }

    public boolean isFiltroAceiteCambiado() {
        return filtroAceiteCambiado;
    }

    public void setFiltroAceiteCambiado(boolean filtroAceiteCambiado) {
        this.filtroAceiteCambiado = filtroAceiteCambiado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProximoServicioKm() {
        return proximoServicioKm;
    }

    public void setProximoServicioKm(Integer proximoServicioKm) {
        this.proximoServicioKm = proximoServicioKm;
    }

    public LocalDate getFechaUltimoCambio() {
        return fechaUltimoCambio;
    }

    public void setFechaUltimoCambio(LocalDate fechaUltimoCambio) {
        this.fechaUltimoCambio = fechaUltimoCambio;
    }

    public LocalDate getFechaProximoCambio() {
        return fechaProximoCambio;
    }

    public void setFechaProximoCambio(LocalDate fechaProximoCambio) {
        this.fechaProximoCambio = fechaProximoCambio;
    }
}
