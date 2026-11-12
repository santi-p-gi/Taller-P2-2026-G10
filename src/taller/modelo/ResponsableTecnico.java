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
        this.especialidad = especialidad;
        this.numeroLicencia = numeroLicencia;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public EquipoRespuesta getEquipoRespuesta() {
        return equipoRespuesta;
    }

    public void setEquipoRespuesta(EquipoRespuesta equipoRespuesta) {
        this.equipoRespuesta = equipoRespuesta;
    }

    public List<EstacionMeteorologica> getEstacionesAsignadas() {
        return estacionesAsignadas;
    }

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

    public List<Mantenimiento> getMantenimientosAsignados() {
        return mantenimientosAsignados;
    }

    public void setMantenimientosAsignados(List<Mantenimiento> mantenimientosAsignados) {
        this.mantenimientosAsignados = mantenimientosAsignados;
    }

    public String toString() {
        return "ResponsableTecnico [especialidad=" + especialidad + ", numeroLicencia=" + numeroLicencia
                + ", equipoRespuesta="
                + (equipoRespuesta != null ? equipoRespuesta.getIdEquipo() : "null") + "]";
    }
}
