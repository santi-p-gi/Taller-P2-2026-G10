package taller.gestion;

import java.util.List;
import java.util.ArrayList;
import taller.modelo.ZonaGeografica;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.Sensor;
import taller.modelo.ResponsableTecnico;
import taller.modelo.EquipoRespuesta;

public class RegistroDatos {

    private List<ZonaGeografica> zonasGeograficas;
    private List<EstacionMeteorologica> estacionesMeteorologicas;
    private List<Sensor> sensores;
    private List<ResponsableTecnico> responsablesTecnicos;
    private List<EquipoRespuesta> equiposRespuesta;

    public RegistroDatos() {
        zonasGeograficas = new ArrayList<>();
        estacionesMeteorologicas = new ArrayList<>();
        sensores = new ArrayList<>();
        responsablesTecnicos = new ArrayList<>();
        equiposRespuesta = new ArrayList<>();
    }

    // ---------------- Zonas geograficas ----------------

    public void agregarZona(ZonaGeografica zona) {
        if (buscarZonaPorId(zona.getIdZona()) != null) {
            throw new IllegalArgumentException("Ya existe una zona con id " + zona.getIdZona());
        }
        zonasGeograficas.add(zona);
    }

    public ZonaGeografica buscarZonaPorId(int idZona) {
        for (ZonaGeografica zona : zonasGeograficas) {
            if (zona.getIdZona() == idZona) {
                return zona;
            }
        }
        return null;
    }

    public List<ZonaGeografica> getZonasGeograficas() {
        return zonasGeograficas;
    }

    // ---------------- Estaciones meteorologicas ----------------

    public void agregarEstacion(EstacionMeteorologica estacion) {
        if (buscarEstacionPorId(estacion.getIdEstacion()) != null) {
            throw new IllegalArgumentException("Ya existe una estacion con id " + estacion.getIdEstacion());
        }
        estacionesMeteorologicas.add(estacion);
    }

    public EstacionMeteorologica buscarEstacionPorId(int idEstacion) {
        for (EstacionMeteorologica estacion : estacionesMeteorologicas) {
            if (estacion.getIdEstacion() == idEstacion) {
                return estacion;
            }
        }
        return null;
    }

    public List<EstacionMeteorologica> getEstacionesMeteorologicas() {
        return estacionesMeteorologicas;
    }

    // ---------------- Sensores ----------------

    public void agregarSensor(Sensor sensor) {
        if (buscarSensorPorId(sensor.getIdSensor()) != null) {
            throw new IllegalArgumentException("Ya existe un sensor con id " + sensor.getIdSensor());
        }
        sensores.add(sensor);
    }

    public Sensor buscarSensorPorId(int idSensor) {
        for (Sensor sensor : sensores) {
            if (sensor.getIdSensor() == idSensor) {
                return sensor;
            }
        }
        return null;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    // ---------------- Responsables tecnicos ----------------

    public void agregarResponsable(ResponsableTecnico responsable) {
        if (buscarResponsablePorDni(responsable.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un responsable con DNI " + responsable.getDni());
        }
        responsablesTecnicos.add(responsable);
    }

    public ResponsableTecnico buscarResponsablePorDni(String dni) {
        for (ResponsableTecnico responsable : responsablesTecnicos) {
            if (responsable.getDni().equals(dni)) {
                return responsable;
            }
        }
        return null;
    }

    public List<ResponsableTecnico> getResponsablesTecnicos() {
        return responsablesTecnicos;
    }

    // ---------------- Equipos de respuesta ----------------

    public void agregarEquipo(EquipoRespuesta equipo) {
        if (buscarEquipoPorId(equipo.getIdEquipo()) != null) {
            throw new IllegalArgumentException("Ya existe un equipo con id " + equipo.getIdEquipo());
        }
        equiposRespuesta.add(equipo);
    }

    public EquipoRespuesta buscarEquipoPorId(int idEquipo) {
        for (EquipoRespuesta equipo : equiposRespuesta) {
            if (equipo.getIdEquipo() == idEquipo) {
                return equipo;
            }
        }
        return null;
    }

    public List<EquipoRespuesta> getEquiposRespuesta() {
        return equiposRespuesta;
    }

}
