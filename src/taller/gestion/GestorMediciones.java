package taller.gestion;

import java.util.List;
import java.time.LocalDateTime;
import taller.modelo.Sensor;
import taller.modelo.Alerta;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.ZonaGeografica;
import taller.modelo.Medicion;

public class GestorMediciones {
    private RegistroDatos registro;

    public GestorMediciones(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- Registro ----------
    public Alerta registrarLectura(int idSensor, double milimetros, LocalDateTime fecha) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        EstacionMeteorologica estacion = sensor.getEstacionMeteorologica();
        if (estacion == null) {
            throw new IllegalArgumentException("El sensor con ID " + idSensor + " no está asociado a ninguna estación");
        }
        ZonaGeografica zona = estacion.getZonaGeografica();
        if (zona == null) {
            throw new IllegalArgumentException(
                    "La estación con ID " + estacion.getIdEstacion() + " no está asociada a ninguna zona geográfica");
        }
        sensor.registrar(milimetros, fecha);
        Alerta alerta = zona.actualizarNivelAlerta(fecha, "Actualización de nivel de alerta", null);
        return alerta;
    }

    // ---------- Consultas ----------
    public List<Medicion> listarMediciones(int idSensor) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        return sensor.getMediciones();

    }

    public List<Medicion> obtenerUltimasMediciones(int idSensor, int cantidad) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        return sensor.obtenerUltimasMediciones(cantidad);
    }

    public Medicion obtenerUltimaMedicion(int idSensor) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        return sensor.obtenerUltimaMedicion();
    }

    public double lluviaAcumulada24hs(int idSensor, LocalDateTime fecha) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        double acumulado = 0.0;
        for (Medicion medicion : sensor.getMediciones()) {
            if (medicion.getFechaHora().isAfter(fecha.minusHours(24)) && !medicion.getFechaHora().isAfter(fecha)) {
                acumulado += medicion.getMilimetros();
            }
        }
        return acumulado;
    }

}