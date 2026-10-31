package taller.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.TipoMantenimiento;

public class Sensor {
    private final String idSensor;
    private String marca;
    private String modelo;
    private double precision;
    private EstadoOperativo estado;
    private LocalDateTime ultimaCalibracion;
    private EstacionMeteorologica estacionMeteorologica;
    private List<Mantenimiento> mantenimientos;
    private List<Medicion> mediciones;

    public Sensor() {
        this.idSensor = java.util.UUID.randomUUID().toString();
        this.estado = EstadoOperativo.OPERATIVA;
        this.mantenimientos = new ArrayList<>();
        this.mediciones = new ArrayList<>();
    }

    public Sensor(String marca, String modelo, double precision, EstadoOperativo estado,
            LocalDateTime ultimaCalibracion, EstacionMeteorologica estacionMeteorologica) {
        this.idSensor = java.util.UUID.randomUUID().toString();
        this.marca = marca;
        this.modelo = modelo;
        this.precision = precision;
        this.estado = estado;
        this.ultimaCalibracion = ultimaCalibracion;
        this.estacionMeteorologica = estacionMeteorologica;
        this.mantenimientos = new ArrayList<>();
        this.mediciones = new ArrayList<>();
    }

    public String getIdSensor() {
        return idSensor;
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

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public EstadoOperativo getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    public LocalDateTime getUltimaCalibracion() {
        return ultimaCalibracion;
    }

    public void setUltimaCalibracion(LocalDateTime ultimaCalibracion) {
        this.ultimaCalibracion = ultimaCalibracion;
    }

    public EstacionMeteorologica getEstacionMeteorologica() {
        return estacionMeteorologica;
    }

    public void setEstacionMeteorologica(EstacionMeteorologica estacionMeteorologica) {
        this.estacionMeteorologica = estacionMeteorologica;
    }

    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimientos.add(mantenimiento);
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    public void registrarMedicion(LocalDateTime fecha, double milimetros, boolean anomala) {
        this.mediciones.add(new Medicion(fecha, milimetros, anomala));
    }

    public boolean necesitaCalibracion() {
        if (this.ultimaCalibracion == null) {
            return true;
        } else {
            LocalDateTime limite = LocalDateTime.now().minusMonths(12);
            return this.ultimaCalibracion.isBefore(limite);
        }
    }

    public boolean validar(double milimetros) {
        return milimetros >= 0.0 && milimetros <= 500.0;
    }

}
