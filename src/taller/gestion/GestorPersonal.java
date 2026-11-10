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

public class GestorPersonal {
    private RegistroDatos registro;

    public GestorPersonal(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------------- Responsables tecnicos ----------------
    public ResponsableTecnico registrarResponsableTecnico(String dni, String nombre, String apellido,
            String correo, String celular, String especialidad, String numeroLicencia) {
        ResponsableTecnico responsable = new ResponsableTecnico(dni, nombre, apellido, correo, celular, especialidad,
                numeroLicencia);
        registro.agregarResponsable(responsable);
        return responsable;
    }

    public ResponsableTecnico buscarResponsablePorDni(String dni) {
        ResponsableTecnico responsable = registro.buscarResponsablePorDni(dni);
        if (responsable == null) {
            throw new IllegalArgumentException("No existe un responsable con DNI " + dni);
        }
        return responsable;
    }

    public List<ResponsableTecnico> getResponsablesTecnicos() {
        return registro.getResponsablesTecnicos();
    }

    public void asignarResponsableAEstacion(String dni, int idEstacion) {
        ResponsableTecnico responsable = buscarResponsablePorDni(dni);
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        estacion.asignarResponsableTecnico(responsable);
    }

    // ---------- Equipos de respuesta ----------

    public EquipoRespuesta registrarEquipoRespuesta(String nombre, boolean disponible) {
        EquipoRespuesta equipo = new EquipoRespuesta(nombre, disponible);
        registro.agregarEquipo(equipo);
        return equipo;
    }

    public EquipoRespuesta buscarEquipoPorId(int idEquipo) {
        EquipoRespuesta equipo = registro.buscarEquipoPorId(idEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("No existe un equipo con ID " + idEquipo);
        }
        return equipo;
    }

    public List<EquipoRespuesta> getEquiposRespuesta() {
        return registro.getEquiposRespuesta();
    }

    public List<EquipoRespuesta> getEquiposDisponibles() {
        List<EquipoRespuesta> equiposDisponibles = new ArrayList<>();
        for (EquipoRespuesta equipo : registro.getEquiposRespuesta()) {
            if (equipo.isDisponible() && equipo.tieneIntegrantes()) {
                equiposDisponibles.add(equipo);
            }
        }
        return equiposDisponibles;
    }

    public void agregarIntegranteAEquipo(int idEquipo, String dniResponsable) {
        EquipoRespuesta equipo = buscarEquipoPorId(idEquipo);
        ResponsableTecnico responsable = buscarResponsablePorDni(dniResponsable);
        equipo.agregarIntegrante(responsable);
    }

    public void eliminarIntegranteDeEquipo(int idEquipo, String dniResponsable) {
        EquipoRespuesta equipo = buscarEquipoPorId(idEquipo);
        ResponsableTecnico responsable = buscarResponsablePorDni(dniResponsable);
        equipo.quitarIntegrante(responsable);
    }

    // ---------------- Mantenimiento y Calibracion ----------------

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
                        + " no está instalado en la estación " + idEstacion);
            }
        }

        if (tipo == TipoMantenimiento.CALIBRACION && sensor == null) {
            throw new IllegalArgumentException("No se puede registrar una calibración sin un sensor asociado.");
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
            throw new IllegalArgumentException("No existe una estación con ID " + idEstacion);
        }
        return estacion;
    }

}
