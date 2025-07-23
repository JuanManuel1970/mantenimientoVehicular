package com.lubricentro.mantenimiento.model;

import jakarta.persistence.*;

@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patente;
    private String marca;
    private String modelo;
    private int kilometros;
    private int duracion_aceite;
    private int duracion_filtros;
    private String email;
    private int proximoServicioKm;

    @PrePersist
    @PreUpdate
    public void calcularProximoServicio() {
        this.proximoServicioKm = this.kilometros + Math.min(duracion_aceite, duracion_filtros);


    }

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
        return duracion_aceite;
    }

    public void setDuracionAceite(int duracionAceite) {
        this.duracion_aceite = duracionAceite;
    }

    public int getDuracionFiltros() {
        return duracion_filtros;
    }

    public void setDuracionFiltros(int duracionFiltros) {
        this.duracion_filtros = duracionFiltros;
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

