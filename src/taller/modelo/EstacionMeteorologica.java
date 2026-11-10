package taller.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.TipoEstacion;

public class EstacionMeteorologica {
    private static int generadorId;
    private int idEstacion;
    private String direccion;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private double altitud;
    private EstadoOperativo estado;
    private TipoEstacion tipo;
    private ZonaGeografica zonaGeografica;
    private List<Sensor> sensores;
    private List<Mantenimiento> mantenimientos;
    private ResponsableTecnico responsableTecnico;

    public EstacionMeteorologica() {
        this.idEstacion = ++generadorId;
        this.estado = EstadoOperativo.OPERATIVA;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
    }

    public EstacionMeteorologica(String direccion, String nombre, Double latitud, Double longitud, double altitud,
            EstadoOperativo estado, TipoEstacion type, ZonaGeografica zonaGeografica,
            ResponsableTecnico responsableTecnico) {
        this.idEstacion = ++generadorId;
        this.direccion = direccion;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
        this.estado = estado;
        this.tipo = type;
        this.zonaGeografica = zonaGeografica;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
        this.responsableTecnico = responsableTecnico;

    }

    public int getIdEstacion() {
        return idEstacion;
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

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
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

    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimientos.add(mantenimiento);
    }

    public double acumuladoEntreSensores(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        double acumulado = 0.0;
        for (Sensor sensor : sensores) {
            acumulado += sensor.acumuladorEntre(fechaInicio, fechaFin);
        }
        return acumulado;
    }

    public ResponsableTecnico getResponsableTecnico() {
        return responsableTecnico;
    }

    public void asignarResponsableTecnico(ResponsableTecnico responsableTecnico) {
        if (this.responsableTecnico != null) {
            this.responsableTecnico.eliminarEstacionMeteorologica(this);
        }
        this.responsableTecnico = responsableTecnico;
        this.responsableTecnico.agregarEstacionMeteorologica(this);
    }

    public void instalarSensor(Sensor sensor) {
        if (sensor.getEstacionMeteorologica() != null) {
            throw new IllegalStateException("El sensor " + sensor.getIdSensor()
                    + " ya está instalado en la estación " + sensor.getEstacionMeteorologica().getIdEstacion() + ".");
        }
        this.sensores.add(sensor);
        sensor.setEstacionMeteorologica(this);
    }

    public void retirarSensor(Sensor sensor) {
        if (this.sensores.remove(sensor)) {
            sensor.setEstacionMeteorologica(null);
        }
    }

    public boolean estaOperativa() {
        return this.estado == EstadoOperativo.OPERATIVA;
    }

    public String toString() {
        return "EstacionMeteorologica [idEstacion=" + idEstacion + ", direccion=" + direccion + ", nombre=" + nombre
                + ", latitud=" + latitud + ", longitud=" + longitud + ", altitud=" + altitud + ", estado=" + estado
                + ", tipo=" + tipo + ", zonaGeografica="
                + (zonaGeografica != null ? zonaGeografica.getNombre() : "null") + "]";
    }
}
