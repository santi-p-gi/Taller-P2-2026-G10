package taller.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Tecnico a cargo de estaciones meteorologicas, que realiza mantenimientos y puede integrar a lo sumo un
 * equipo de respuesta.
 */
public class ResponsableTecnico extends Persona {
    private String especialidad;
    private String numeroLicencia;
    private EquipoRespuesta equipoRespuesta;
    private List<EstacionMeteorologica> estacionesAsignadas;
    private List<Mantenimiento> mantenimientosAsignados;

    /**
     * Crea un responsable tecnico sin especialidad ni licencia.
     *
     * @param dni               documento
     * @param nombre            nombre
     * @param apellido          apellido
     * @param correoElectronico correo electronico
     * @param celular           numero de celular
     */
    public ResponsableTecnico(String dni, String nombre, String apellido, String correoElectronico, String celular) {
        super(dni, nombre, apellido, correoElectronico, celular);
        this.estacionesAsignadas = new ArrayList<>();
        this.mantenimientosAsignados = new ArrayList<>();
    }

    /**
     * Crea un responsable tecnico con todos sus datos.
     *
     * @param dni               documento
     * @param nombre            nombre
     * @param apellido          apellido
     * @param correoElectronico correo electronico
     * @param celular           numero de celular
     * @param especialidad      especialidad tecnica
     * @param numeroLicencia    numero de licencia profesional
     */
    public ResponsableTecnico(String dni, String nombre, String apellido, String correoElectronico, String celular,
            String especialidad,
            String numeroLicencia) {
        this(dni, nombre, apellido, correoElectronico, celular);
        if (especialidad == null || !especialidad.matches("[\\p{L} ]+")) {
            throw new IllegalArgumentException("La especialidad solo puede contener letras y espacios.");
        }
        this.especialidad = especialidad;
        this.numeroLicencia = numeroLicencia;
    }

    /** {@return la especialidad del responsable} */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Modifica la especialidad del responsable.
     *
     * @param especialidad el nuevo valor
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /** {@return el numero de licencia del responsable} */
    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    /**
     * Modifica el numero de licencia del responsable.
     *
     * @param numeroLicencia el nuevo valor
     */
    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    /** {@return el equipo al que pertenece el responsable} */
    public EquipoRespuesta getEquipoRespuesta() {
        return equipoRespuesta;
    }

    /**
     * Modifica el equipo al que pertenece el responsable.
     *
     * @param equipoRespuesta el nuevo valor
     */
    public void setEquipoRespuesta(EquipoRespuesta equipoRespuesta) {
        this.equipoRespuesta = equipoRespuesta;
    }

    /** {@return las estaciones a cargo del responsable} */
    public List<EstacionMeteorologica> getEstacionesAsignadas() {
        return estacionesAsignadas;
    }

    /**
     * Modifica las estaciones a cargo del responsable.
     *
     * @param estacionesAsignadas el nuevo valor
     */
    public void setEstacionesAsignadas(List<EstacionMeteorologica> estacionesAsignadas) {
        this.estacionesAsignadas = estacionesAsignadas;
    }

    /**
     * Agrega una estacion a la lista de estaciones a cargo del tecnico.
     *
     * @param estacion estacion a agregar
     */
    public void agregarEstacionMeteorologica(EstacionMeteorologica estacion) {
        this.estacionesAsignadas.add(estacion);
    }

    /**
     * Quita una estacion de la lista de estaciones a cargo del tecnico.
     *
     * @param estacion estacion a quitar
     */
    public void eliminarEstacionMeteorologica(EstacionMeteorologica estacion) {
        this.estacionesAsignadas.remove(estacion);
    }

    /**
     * Agrega un mantenimiento a la lista de mantenimientos realizados por el tecnico.
     *
     * @param mantenimiento mantenimiento a agregar
     */
    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimientosAsignados.add(mantenimiento);
    }

    /**
     * Indica si el tecnico ya integra un equipo de respuesta.
     *
     * @return true si pertenece a un equipo
     */
    public boolean estaEnEquipoRespuesta() {
        return equipoRespuesta != null;
    }

    /** {@return los mantenimientos realizados por el responsable} */
    public List<Mantenimiento> getMantenimientosAsignados() {
        return mantenimientosAsignados;
    }

    /**
     * Modifica los mantenimientos realizados por el responsable.
     *
     * @param mantenimientosAsignados el nuevo valor
     */
    public void setMantenimientosAsignados(List<Mantenimiento> mantenimientosAsignados) {
        this.mantenimientosAsignados = mantenimientosAsignados;
    }

    public String toString() {
        return "ResponsableTecnico [especialidad=" + especialidad + ", numeroLicencia=" + numeroLicencia
                + ", equipoRespuesta="
                + (equipoRespuesta != null ? equipoRespuesta.getIdEquipo() : "null") + "]";
    }
}
