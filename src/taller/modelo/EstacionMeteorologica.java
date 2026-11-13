package taller.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.TipoEstacion;

/**
 * Estacion meteorologica de una zona, con sus sensores instalados y su responsable tecnico a cargo.
 */
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

    /**
     * Crea una estacion operativa y vacia, con ID autogenerado.
     */
    public EstacionMeteorologica() {
        this.idEstacion = ++generadorId;
        this.estado = EstadoOperativo.OPERATIVA;
        this.sensores = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
    }

    /**
     * Crea una estacion con ID autogenerado. No actualiza la zona ni el responsable: para eso usar
     * {@link ZonaGeografica#agregarEstacionMeteorologica} y {@link #asignarResponsableTecnico}.
     *
     * @param direccion          direccion de la estacion
     * @param nombre             nombre de la estacion
     * @param latitud            latitud
     * @param longitud           longitud
     * @param altitud            altitud en metros
     * @param estado             estado operativo inicial
     * @param type               tipo de estacion
     * @param zonaGeografica     zona a la que pertenece
     * @param responsableTecnico tecnico a cargo
     */
    public EstacionMeteorologica(String direccion, String nombre, Double latitud, Double longitud, double altitud,
            EstadoOperativo estado, TipoEstacion type, ZonaGeografica zonaGeografica,
            ResponsableTecnico responsableTecnico) {
        if (nombre == null || !nombre.matches(".*\\p{L}.*")) {
            throw new IllegalArgumentException("El nombre de la estacion debe contener al menos una letra.");
        }
        if (latitud == null || latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90.");
        }
        if (longitud == null || longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180.");
        }
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

    /** {@return el identificador de la estacion} */
    public int getIdEstacion() {
        return idEstacion;
    }

    /** {@return la direccion de la estacion} */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Modifica la direccion de la estacion.
     *
     * @param direccion el nuevo valor
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** {@return el nombre de la estacion} */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre de la estacion.
     *
     * @param nombre el nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** {@return la latitud de la estacion} */
    public Double getLatitud() {
        return latitud;
    }

    /**
     * Modifica la latitud de la estacion.
     *
     * @param latitud el nuevo valor
     */
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    /** {@return la longitud de la estacion} */
    public Double getLongitud() {
        return longitud;
    }

    /**
     * Modifica la longitud de la estacion.
     *
     * @param longitud el nuevo valor
     */
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    /** {@return la altitud de la estacion, en metros} */
    public double getAltitud() {
        return altitud;
    }

    /**
     * Modifica la altitud de la estacion, en metros.
     *
     * @param altitud el nuevo valor
     */
    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    /** {@return el estado operativo de la estacion} */
    public EstadoOperativo getEstado() {
        return estado;
    }

    /**
     * Modifica el estado operativo de la estacion.
     *
     * @param estado el nuevo valor
     */
    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    /** {@return el tipo de estacion} */
    public TipoEstacion getTipo() {
        return tipo;
    }

    /**
     * Modifica el tipo de estacion.
     *
     * @param tipo el nuevo valor
     */
    public void setTipo(TipoEstacion tipo) {
        this.tipo = tipo;
    }

    /** {@return la zona geografica de la estacion} */
    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    /**
     * Modifica la zona geografica de la estacion.
     *
     * @param zonaGeografica el nuevo valor
     */
    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

    /** {@return los sensores instalados en la estacion} */
    public List<Sensor> getSensores() {
        return sensores;
    }

    /**
     * Modifica los sensores instalados en la estacion.
     *
     * @param sensores el nuevo valor
     */
    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    /** {@return los mantenimientos de la estacion} */
    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    /**
     * Modifica los mantenimientos de la estacion.
     *
     * @param mantenimientos el nuevo valor
     */
    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    /**
     * Agrega un mantenimiento al historial de la estacion.
     *
     * @param mantenimiento mantenimiento a agregar
     */
    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimientos.add(mantenimiento);
    }

    /**
     * Suma los milimetros registrados por todos los sensores de la estacion en un rango de fechas.
     *
     * @param fechaInicio inicio del rango (incluido)
     * @param fechaFin    fin del rango (incluido)
     * @return milimetros acumulados
     */
    public double acumuladoEntreSensores(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        double acumulado = 0.0;
        for (Sensor sensor : sensores) {
            acumulado += sensor.acumuladorEntre(fechaInicio, fechaFin);
        }
        return acumulado;
    }

    /** {@return el responsable tecnico de la estacion} */
    public ResponsableTecnico getResponsableTecnico() {
        return responsableTecnico;
    }

    /**
     * Pone la estacion a cargo de un tecnico, actualizando tambien al responsable anterior y al nuevo.
     *
     * @param responsableTecnico nuevo responsable
     */
    public void asignarResponsableTecnico(ResponsableTecnico responsableTecnico) {
        if (this.responsableTecnico != null) {
            this.responsableTecnico.eliminarEstacionMeteorologica(this);
        }
        this.responsableTecnico = responsableTecnico;
        this.responsableTecnico.agregarEstacionMeteorologica(this);
    }

    /**
     * Instala un sensor en la estacion (asignacion unica).
     *
     * @param sensor sensor a instalar
     * @throws IllegalStateException si el sensor ya esta instalado en una estacion
     */
    public void instalarSensor(Sensor sensor) {
        if (sensor.getEstacionMeteorologica() != null) {
            throw new IllegalStateException("El sensor " + sensor.getIdSensor()
                    + " ya esta instalado en la estacion " + sensor.getEstacionMeteorologica().getIdEstacion() + ".");
        }
        this.sensores.add(sensor);
        sensor.setEstacionMeteorologica(this);
    }

    /**
     * Retira un sensor de la estacion, si estaba instalado en ella.
     *
     * @param sensor sensor a retirar
     */
    public void retirarSensor(Sensor sensor) {
        if (this.sensores.remove(sensor)) {
            sensor.setEstacionMeteorologica(null);
        }
    }

    /**
     * Indica si la estacion esta operativa.
     *
     * @return true si su estado es OPERATIVA
     */
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
