package taller.modelo;

import java.time.LocalDateTime;

import taller.enumeraciones.TipoMantenimiento;

/**
 * Mantenimiento realizado por un responsable tecnico sobre una estacion, y opcionalmente sobre un sensor
 * puntual de esa estacion.
 */
public class Mantenimiento {
    private static int generadorId;
    private final int idMantenimiento;
    private LocalDateTime fecha;
    private String descripcion;
    private TipoMantenimiento tipo;
    private Sensor sensor;
    private ResponsableTecnico responsableTecnico;
    private EstacionMeteorologica estacionMeteorologica;

    /**
     * Crea un mantenimiento vacio con ID autogenerado.
     */
    public Mantenimiento() {
        this.idMantenimiento = ++generadorId;
    }

    /**
     * Crea un mantenimiento con ID autogenerado.
     *
     * @param fecha                 fecha en que se realizo
     * @param descripcion           detalle del trabajo realizado
     * @param tipo                  tipo de mantenimiento
     * @param sensor                sensor intervenido, o null si es de la estacion en general
     * @param responsableTecnico    tecnico que lo realizo
     * @param estacionMeteorologica estacion a la que pertenece
     */
    public Mantenimiento(LocalDateTime fecha, String descripcion, TipoMantenimiento tipo,
            Sensor sensor, ResponsableTecnico responsableTecnico, EstacionMeteorologica estacionMeteorologica) {
        this.idMantenimiento = ++generadorId;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estacionMeteorologica = estacionMeteorologica;
        this.sensor = sensor;
        this.responsableTecnico = responsableTecnico;

    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstacionMeteorologica getEstacionMeteorologica() {
        return estacionMeteorologica;
    }

    public void setEstacionMeteorologica(EstacionMeteorologica estacionMeteorologica) {
        this.estacionMeteorologica = estacionMeteorologica;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMantenimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMantenimiento tipo) {
        this.tipo = tipo;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public ResponsableTecnico getResponsableTecnico() {
        return responsableTecnico;
    }

    public void setResponsableTecnico(ResponsableTecnico responsableTecnico) {
        this.responsableTecnico = responsableTecnico;
    }

    public String toString() {
        return "Mantenimiento [idMantenimiento=" + idMantenimiento + ", fecha=" + fecha + ", descripcion=" + descripcion
                + ", tipo=" + tipo + ", sensor=" + (sensor != null ? sensor.getIdSensor() : "null")
                + ", responsableTecnico="
                + (responsableTecnico != null ? responsableTecnico.getNombreCompleto() : "null")
                + ", estacionMeteorologica="
                + (estacionMeteorologica != null ? estacionMeteorologica.getIdEstacion() : "null") + "]";
    }
}
