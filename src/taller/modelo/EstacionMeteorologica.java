package taller.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.TipoEstacion;
import taller.enumeraciones.TipoMantenimiento;

public class EstacionMeteorologica {
    private String direccion;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private EstadoOperativo estado;
    private TipoEstacion tipo;
    private ZonaGeografica zonaGeografica;
    private List<Sensor> sensores;
    private List<Mantenimiento> mantenimientos;
    private ResponsableTecnico responsableTecnico;

    public EstacionMeteorologica() {
        this.estado = EstadoOperativo.OPERATIVA;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
    }

    public EstacionMeteorologica(String direccion, String nombre, Double latitud, Double longitud,
            EstadoOperativo estado, TipoEstacion type, ZonaGeografica zonaGeografica,
            ResponsableTecnico responsableTecnico) {
        this.direccion = direccion;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.tipo = type;
        this.zonaGeografica = zonaGeografica;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
        this.responsableTecnico = responsableTecnico;

    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public EstadoOperativo getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    public TipoEstacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstacion tipo) {
        this.tipo = tipo;
    }

    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public void agregarSensor(Sensor sensor) {
        this.sensores.add(sensor);
    }

    public void eliminarSensor(Sensor sensor) {
        this.sensores.remove(sensor);
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

    public ResponsableTecnico getResponsableTecnico() {
        return responsableTecnico;
    }

    public void setResponsableTecnico(ResponsableTecnico responsableTecnico) {
        this.responsableTecnico = responsableTecnico;
    }

    public void agregarResponsableTecnico(ResponsableTecnico responsableTecnico) {
        this.responsableTecnico = responsableTecnico;
    }

}
