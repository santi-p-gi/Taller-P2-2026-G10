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
        if (fecha.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del mantenimiento no puede ser posterior a la fecha actual.");
        }
        this.idMantenimiento = ++generadorId;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estacionMeteorologica = estacionMeteorologica;
        this.sensor = sensor;
        this.responsableTecnico = responsableTecnico;

    }

    /** {@return el identificador del mantenimiento} */
    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    /** {@return la fecha del mantenimiento} */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /** {@return la estacion del mantenimiento} */
    public EstacionMeteorologica getEstacionMeteorologica() {
        return estacionMeteorologica;
    }

    /**
     * Modifica la estacion del mantenimiento.
     *
     * @param estacionMeteorologica el nuevo valor
     */
    public void setEstacionMeteorologica(EstacionMeteorologica estacionMeteorologica) {
        this.estacionMeteorologica = estacionMeteorologica;
    }

    /**
     * Modifica la fecha del mantenimiento.
     *
     * @param fecha el nuevo valor
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /** {@return la descripcion del mantenimiento} */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Modifica la descripcion del mantenimiento.
     *
     * @param descripcion el nuevo valor
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** {@return el tipo de mantenimiento} */
    public TipoMantenimiento getTipo() {
        return tipo;
    }

    /**
     * Modifica el tipo de mantenimiento.
     *
     * @param tipo el nuevo valor
     */
    public void setTipo(TipoMantenimiento tipo) {
        this.tipo = tipo;
    }

    /** {@return el sensor intervenido, o null si fue sobre la estacion} */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Modifica el sensor intervenido, o null si fue sobre la estacion.
     *
     * @param sensor el nuevo valor
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /** {@return el responsable tecnico del mantenimiento} */
    public ResponsableTecnico getResponsableTecnico() {
        return responsableTecnico;
    }

    /**
     * Modifica el responsable tecnico del mantenimiento.
     *
     * @param responsableTecnico el nuevo valor
     */
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
