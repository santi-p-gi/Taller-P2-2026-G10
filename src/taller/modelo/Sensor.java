package taller.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.TipoMantenimiento;

public class Sensor {
    private String idSensor;
    private String marca;
    private String modelo;
    private double precision;
    private EstadoOperativo estado;
    private LocalDateTime ultimaCalibracion;
    private EstacionMeteorologica estacionMeteorologica;
    private List<Mantenimiento> mantenimientos;
    private List<Medicion> mediciones;

    public Sensor() {
        this.estado = EstadoOperativo.OPERATIVA;
        this.mantenimientos = new ArrayList<>();
        this.mediciones = new ArrayList<>();
    }

    public Sensor(String idSensor, String marca, String modelo, double precision, EstadoOperativo estado,
            LocalDateTime ultimaCalibracion, EstacionMeteorologica estacionMeteorologica) {
        this.idSensor = idSensor;
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

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
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

    public void agregarMantenimiento(String idMantenimiento, LocalDateTime fecha, String descripcion,
            TipoMantenimiento tipo, Sensor sensor, ResponsableTecnico responsableTecnico) {
        this.mantenimientos
                .add(new Mantenimiento(idMantenimiento, fecha, descripcion, tipo, sensor, responsableTecnico));
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    public void agregarMedicion(String idMedicion, LocalDateTime fecha, double milimetros, boolean anomala) {
        this.mediciones.add(new Medicion(idMedicion, fecha, milimetros, anomala));
    }

}
