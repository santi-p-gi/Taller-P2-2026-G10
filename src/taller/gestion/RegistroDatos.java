package taller.gestion;

import java.util.List;
import java.util.ArrayList;
import taller.modelo.ZonaGeografica;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.Sensor;
import taller.modelo.ResponsableTecnico;
import taller.modelo.EquipoRespuesta;

/**
 * Almacenamiento en memoria de las entidades principales del sistema. Es compartido por todos los gestores.
 * Las entidades dependientes (mediciones, alertas, mantenimientos, valores historicos) se guardan dentro
 * de la entidad a la que pertenecen.
 */
public class RegistroDatos {

    private List<ZonaGeografica> zonasGeograficas;
    private List<EstacionMeteorologica> estacionesMeteorologicas;
    private List<Sensor> sensores;
    private List<ResponsableTecnico> responsablesTecnicos;
    private List<EquipoRespuesta> equiposRespuesta;

    /**
     * Crea un registro con todas las listas vacias.
     */
    public RegistroDatos() {
        zonasGeograficas = new ArrayList<>();
        estacionesMeteorologicas = new ArrayList<>();
        sensores = new ArrayList<>();
        responsablesTecnicos = new ArrayList<>();
        equiposRespuesta = new ArrayList<>();
    }

    // ---------------- Zonas geograficas ----------------

    /**
     * Agrega una zona al registro.
     *
     * @param zona zona a agregar
     * @throws IllegalArgumentException si ya existe una zona con ese ID
     */
    public void agregarZona(ZonaGeografica zona) {
        if (buscarZonaPorId(zona.getIdZona()) != null) {
            throw new IllegalArgumentException("Ya existe una zona con id " + zona.getIdZona());
        }
        zonasGeograficas.add(zona);
    }

    /**
     * Busca una zona por su ID.
     *
     * @param idZona ID de la zona
     * @return la zona, o null si no existe
     */
    public ZonaGeografica buscarZonaPorId(int idZona) {
        for (ZonaGeografica zona : zonasGeograficas) {
            if (zona.getIdZona() == idZona) {
                return zona;
            }
        }
        return null;
    }

    /**
     * Devuelve todas las zonas registradas.
     *
     * @return lista de zonas
     */
    public List<ZonaGeografica> getZonasGeograficas() {
        return zonasGeograficas;
    }

    // ---------------- Estaciones meteorologicas ----------------

    /**
     * Agrega una estacion al registro.
     *
     * @param estacion estacion a agregar
     * @throws IllegalArgumentException si ya existe una estacion con ese ID
     */
    public void agregarEstacion(EstacionMeteorologica estacion) {
        if (buscarEstacionPorId(estacion.getIdEstacion()) != null) {
            throw new IllegalArgumentException("Ya existe una estacion con id " + estacion.getIdEstacion());
        }
        estacionesMeteorologicas.add(estacion);
    }

    /**
     * Busca una estacion por su ID.
     *
     * @param idEstacion ID de la estacion
     * @return la estacion, o null si no existe
     */
    public EstacionMeteorologica buscarEstacionPorId(int idEstacion) {
        for (EstacionMeteorologica estacion : estacionesMeteorologicas) {
            if (estacion.getIdEstacion() == idEstacion) {
                return estacion;
            }
        }
        return null;
    }

    /**
     * Devuelve todas las estaciones registradas.
     *
     * @return lista de estaciones
     */
    public List<EstacionMeteorologica> getEstacionesMeteorologicas() {
        return estacionesMeteorologicas;
    }

    // ---------------- Sensores ----------------

    /**
     * Agrega un sensor al registro.
     *
     * @param sensor sensor a agregar
     * @throws IllegalArgumentException si ya existe un sensor con ese ID
     */
    public void agregarSensor(Sensor sensor) {
        if (buscarSensorPorId(sensor.getIdSensor()) != null) {
            throw new IllegalArgumentException("Ya existe un sensor con id " + sensor.getIdSensor());
        }
        sensores.add(sensor);
    }

    /**
     * Busca un sensor por su ID.
     *
     * @param idSensor ID del sensor
     * @return el sensor, o null si no existe
     */
    public Sensor buscarSensorPorId(int idSensor) {
        for (Sensor sensor : sensores) {
            if (sensor.getIdSensor() == idSensor) {
                return sensor;
            }
        }
        return null;
    }

    /**
     * Devuelve todos los sensores registrados, instalados o no.
     *
     * @return lista de sensores
     */
    public List<Sensor> getSensores() {
        return sensores;
    }

    // ---------------- Responsables tecnicos ----------------

    /**
     * Agrega un responsable tecnico al registro.
     *
     * @param responsable responsable a agregar
     * @throws IllegalArgumentException si ya existe un responsable con ese DNI
     */
    public void agregarResponsable(ResponsableTecnico responsable) {
        if (buscarResponsablePorDni(responsable.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un responsable con DNI " + responsable.getDni());
        }
        responsablesTecnicos.add(responsable);
    }

    /**
     * Busca un responsable tecnico por su DNI.
     *
     * @param dni DNI del responsable
     * @return el responsable, o null si no existe
     */
    public ResponsableTecnico buscarResponsablePorDni(String dni) {
        for (ResponsableTecnico responsable : responsablesTecnicos) {
            if (responsable.getDni().equals(dni)) {
                return responsable;
            }
        }
        return null;
    }

    /**
     * Devuelve todos los responsables tecnicos registrados.
     *
     * @return lista de responsables
     */
    public List<ResponsableTecnico> getResponsablesTecnicos() {
        return responsablesTecnicos;
    }

    // ---------------- Equipos de respuesta ----------------

    /**
     * Agrega un equipo de respuesta al registro.
     *
     * @param equipo equipo a agregar
     * @throws IllegalArgumentException si ya existe un equipo con ese ID
     */
    public void agregarEquipo(EquipoRespuesta equipo) {
        if (buscarEquipoPorId(equipo.getIdEquipo()) != null) {
            throw new IllegalArgumentException("Ya existe un equipo con id " + equipo.getIdEquipo());
        }
        equiposRespuesta.add(equipo);
    }

    /**
     * Busca un equipo de respuesta por su ID.
     *
     * @param idEquipo ID del equipo
     * @return el equipo, o null si no existe
     */
    public EquipoRespuesta buscarEquipoPorId(int idEquipo) {
        for (EquipoRespuesta equipo : equiposRespuesta) {
            if (equipo.getIdEquipo() == idEquipo) {
                return equipo;
            }
        }
        return null;
    }

    /**
     * Devuelve todos los equipos de respuesta registrados.
     *
     * @return lista de equipos
     */
    public List<EquipoRespuesta> getEquiposRespuesta() {
        return equiposRespuesta;
    }

}
