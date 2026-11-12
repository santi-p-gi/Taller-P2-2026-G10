package taller.gestion;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

import taller.enumeraciones.TipoMantenimiento;
import taller.modelo.ResponsableTecnico;
import taller.modelo.Sensor;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.EquipoRespuesta;
import taller.modelo.Mantenimiento;

/**
 * Administracion del personal tecnico: responsables, equipos de respuesta y mantenimientos.
 */
public class GestorPersonal {
    private RegistroDatos registro;

    /**
     * Crea el gestor sobre el registro de datos del sistema.
     *
     * @param registro registro de datos compartido
     */
    public GestorPersonal(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------------- Responsables tecnicos ----------------

    /**
     * Registra un responsable tecnico.
     *
     * @param dni            DNI, no puede repetirse
     * @param nombre         nombre
     * @param apellido       apellido
     * @param correo         correo electronico
     * @param celular        numero de celular
     * @param especialidad   especialidad tecnica
     * @param numeroLicencia numero de licencia profesional
     * @return el responsable registrado
     */
    public ResponsableTecnico registrarResponsableTecnico(String dni, String nombre, String apellido,
            String correo, String celular, String especialidad, String numeroLicencia) {
        ResponsableTecnico responsable = new ResponsableTecnico(dni, nombre, apellido, correo, celular, especialidad,
                numeroLicencia);
        registro.agregarResponsable(responsable);
        return responsable;
    }

    /**
     * Busca un responsable tecnico por su DNI.
     *
     * @param dni DNI del responsable
     * @return el responsable
     * @throws IllegalArgumentException si no existe
     */
    public ResponsableTecnico buscarResponsablePorDni(String dni) {
        ResponsableTecnico responsable = registro.buscarResponsablePorDni(dni);
        if (responsable == null) {
            throw new IllegalArgumentException("No existe un responsable con DNI " + dni);
        }
        return responsable;
    }

    /**
     * Devuelve todos los responsables tecnicos.
     *
     * @return lista de responsables
     */
    public List<ResponsableTecnico> getResponsablesTecnicos() {
        return registro.getResponsablesTecnicos();
    }

    /**
     * Pone una estacion a cargo de un responsable tecnico, reemplazando al anterior.
     *
     * @param dni        DNI del responsable
     * @param idEstacion ID de la estacion
     */
    public void asignarResponsableAEstacion(String dni, int idEstacion) {
        ResponsableTecnico responsable = buscarResponsablePorDni(dni);
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        estacion.asignarResponsableTecnico(responsable);
    }

    // ---------- Equipos de respuesta ----------

    /**
     * Registra un equipo de respuesta sin integrantes.
     *
     * @param nombre     nombre del equipo
     * @param disponible si el equipo puede recibir alertas
     * @return el equipo registrado
     */
    public EquipoRespuesta registrarEquipoRespuesta(String nombre, boolean disponible) {
        EquipoRespuesta equipo = new EquipoRespuesta(nombre, disponible);
        registro.agregarEquipo(equipo);
        return equipo;
    }

    /**
     * Busca un equipo de respuesta por su ID.
     *
     * @param idEquipo ID del equipo
     * @return el equipo
     * @throws IllegalArgumentException si no existe
     */
    public EquipoRespuesta buscarEquipoPorId(int idEquipo) {
        EquipoRespuesta equipo = registro.buscarEquipoPorId(idEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("No existe un equipo con ID " + idEquipo);
        }
        return equipo;
    }

    /**
     * Devuelve todos los equipos de respuesta.
     *
     * @return lista de equipos
     */
    public List<EquipoRespuesta> getEquiposRespuesta() {
        return registro.getEquiposRespuesta();
    }

    /**
     * Devuelve los equipos que pueden recibir una alerta: disponibles y con integrantes.
     *
     * @return equipos disponibles
     */
    public List<EquipoRespuesta> getEquiposDisponibles() {
        List<EquipoRespuesta> equiposDisponibles = new ArrayList<>();
        for (EquipoRespuesta equipo : registro.getEquiposRespuesta()) {
            if (equipo.isDisponible() && equipo.tieneIntegrantes()) {
                equiposDisponibles.add(equipo);
            }
        }
        return equiposDisponibles;
    }

    /**
     * Agrega un responsable tecnico a un equipo.
     *
     * @param idEquipo       ID del equipo
     * @param dniResponsable DNI del responsable
     */
    public void agregarIntegranteAEquipo(int idEquipo, String dniResponsable) {
        EquipoRespuesta equipo = buscarEquipoPorId(idEquipo);
        ResponsableTecnico responsable = buscarResponsablePorDni(dniResponsable);
        equipo.agregarIntegrante(responsable);
    }

    /**
     * Quita un responsable tecnico de un equipo.
     *
     * @param idEquipo       ID del equipo
     * @param dniResponsable DNI del responsable
     */
    public void eliminarIntegranteDeEquipo(int idEquipo, String dniResponsable) {
        EquipoRespuesta equipo = buscarEquipoPorId(idEquipo);
        ResponsableTecnico responsable = buscarResponsablePorDni(dniResponsable);
        equipo.quitarIntegrante(responsable);
    }

    // ---------------- Mantenimiento y Calibracion ----------------

    /**
     * Registra un mantenimiento de una estacion, opcionalmente sobre uno de sus sensores. Si es una
     * CALIBRACION, actualiza la fecha de calibracion del sensor.
     *
     * @param idEstacion     ID de la estacion
     * @param idSensor       ID del sensor intervenido, o null (obligatorio para CALIBRACION)
     * @param dniResponsable DNI del responsable que lo realizo
     * @param tipo           tipo de mantenimiento
     * @param fecha          fecha del mantenimiento
     * @param descripcion    detalle del trabajo
     * @return el mantenimiento registrado
     * @throws IllegalArgumentException si el sensor no existe, no esta en la estacion o falta para una CALIBRACION
     */
    public Mantenimiento registrarMantenimiento(int idEstacion, Integer idSensor, String dniResponsable,
            TipoMantenimiento tipo, LocalDateTime fecha, String descripcion) {
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        ResponsableTecnico responsable = buscarResponsablePorDni(dniResponsable);

        Sensor sensor = null;
        if (idSensor != null) {
            sensor = registro.buscarSensorPorId(idSensor);
            if (sensor == null) {
                throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
            }
            if (sensor.getEstacionMeteorologica() != estacion) {
                throw new IllegalArgumentException("El sensor " + idSensor
                        + " no esta instalado en la estacion " + idEstacion);
            }
        }

        if (tipo == TipoMantenimiento.CALIBRACION && sensor == null) {
            throw new IllegalArgumentException("No se puede registrar una calibracion sin un sensor asociado.");
        }
        Mantenimiento mantenimiento = new Mantenimiento(fecha, descripcion, tipo, sensor, responsable, estacion);

        estacion.agregarMantenimiento(mantenimiento);
        responsable.agregarMantenimiento(mantenimiento);
        if (sensor != null) {
            sensor.agregarMantenimiento(mantenimiento);
        }

        if (tipo == TipoMantenimiento.CALIBRACION) {
            sensor.calibrar(fecha);
        }

        return mantenimiento;
    }

    // ---------------- Auxiliares ----------------

    private EstacionMeteorologica buscarEstacionPorId(int idEstacion) {
        EstacionMeteorologica estacion = registro.buscarEstacionPorId(idEstacion);
        if (estacion == null) {
            throw new IllegalArgumentException("No existe una estacion con ID " + idEstacion);
        }
        return estacion;
    }

}
