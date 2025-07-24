package com.lubricentro.mantenimiento.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCliente;
    private String patente;
    private String marca;
    private String modelo;
    private int kilometros;
    private int duracionAceite;
    private int kmPorMes;
    private boolean filtroAireCambiado;
    private boolean filtroCombustibleCambiado;
    private boolean filtroAceiteCambiado;
    private String email;

    private int proximoServicioKm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaUltimoCambio;

    private LocalDate fechaProximoCambio;


    public void calcularProximoMantenimiento() {
        this.proximoServicioKm = this.kilometros + this.duracionAceite;

        if (fechaUltimoCambio != null && kmPorMes > 0) {
            double mesesDuracion = (double) duracionAceite / kmPorMes;
            int diasDuracion = (int) Math.floor(mesesDuracion * 30);

            this.fechaProximoCambio = fechaUltimoCambio.plusDays(diasDuracion);
        } else {
            this.fechaProximoCambio = null;
        }
    }





    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    public int getKmPorMes() {
        return kmPorMes;
    }

    public void setKmPorMes(int kmPorMes) {
        this.kmPorMes = kmPorMes;
    }



    // === Getters y Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getKilometros() {
        return kilometros;
    }

    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }

    public int getDuracionAceite() {
        return duracionAceite;
    }

    public void setDuracionAceite(int duracionAceite) {
        this.duracionAceite = duracionAceite;
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

    public int getProximoServicioKm() {
        return proximoServicioKm;
    }

    public void setProximoServicioKm(int proximoServicioKm) {
        this.proximoServicioKm = proximoServicioKm;
    }
}
