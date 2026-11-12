package taller.gestion;

import java.util.List;
import java.time.LocalDateTime;
import taller.modelo.Sensor;
import taller.modelo.Alerta;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.ZonaGeografica;
import taller.modelo.Medicion;

/**
 * Registro y consulta de las lecturas de precipitacion de los sensores.
 */
public class GestorMediciones {
    private RegistroDatos registro;

    /**
     * Crea el gestor sobre el registro de datos del sistema.
     *
     * @param registro registro de datos compartido
     */
    public GestorMediciones(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- Registro ----------

    /**
     * Registra una lectura en un sensor y actualiza el nivel de alerta de su zona.
     *
     * @param idSensor   ID del sensor
     * @param milimetros milimetros registrados
     * @param fecha      fecha y hora de la lectura
     * @return la alerta LLUVIA_COPIOSA emitida si el nivel de la zona subio, o null
     * @throws IllegalArgumentException si el sensor no existe, no esta instalado o su estacion no tiene zona
     */
    public Alerta registrarLectura(int idSensor, double milimetros, LocalDateTime fecha) {
        Sensor sensor = buscarSensor(idSensor);
        EstacionMeteorologica estacion = sensor.getEstacionMeteorologica();
        if (estacion == null) {
            throw new IllegalArgumentException("El sensor con ID " + idSensor + " no esta asociado a ninguna estacion");
        }
        ZonaGeografica zona = estacion.getZonaGeografica();
        if (zona == null) {
            throw new IllegalArgumentException(
                    "La estacion con ID " + estacion.getIdEstacion() + " no esta asociada a ninguna zona geografica");
        }
        sensor.registrar(milimetros, fecha);
        Alerta alerta = zona.actualizarNivelAlerta(fecha, "Actualizacion de nivel de alerta", null);
        return alerta;
    }

    // ---------- Consultas ----------

    /**
     * Devuelve todas las mediciones de un sensor.
     *
     * @param idSensor ID del sensor
     * @return mediciones del sensor
     */
    public List<Medicion> listarMediciones(int idSensor) {
        Sensor sensor = buscarSensor(idSensor);
        return sensor.getMediciones();
    }

    /**
     * Devuelve las ultimas mediciones de un sensor.
     *
     * @param idSensor ID del sensor
     * @param cantidad cantidad maxima de mediciones
     * @return ultimas mediciones del sensor
     */
    public List<Medicion> obtenerUltimasMediciones(int idSensor, int cantidad) {
        Sensor sensor = buscarSensor(idSensor);
        return sensor.obtenerUltimasMediciones(cantidad);
    }

    /**
     * Devuelve la ultima medicion de un sensor.
     *
     * @param idSensor ID del sensor
     * @return ultima medicion, o null si no tiene
     */
    public Medicion obtenerUltimaMedicion(int idSensor) {
        Sensor sensor = buscarSensor(idSensor);
        return sensor.obtenerUltimaMedicion();
    }

    /**
     * Suma la lluvia registrada por un sensor en las 24 horas previas a una fecha.
     *
     * @param idSensor ID del sensor
     * @param fecha    fin de la ventana de 24 horas
     * @return milimetros acumulados
     */
    public double lluviaAcumulada24hs(int idSensor, LocalDateTime fecha) {
        Sensor sensor = buscarSensor(idSensor);
        double acumulado = 0.0;
        for (Medicion medicion : sensor.getMediciones()) {
            if (medicion.getFechaHora().isAfter(fecha.minusHours(24)) && !medicion.getFechaHora().isAfter(fecha)) {
                acumulado += medicion.getMilimetros();
            }
        }
        return acumulado;
    }

    // ---------- Auxiliares ----------

    private Sensor buscarSensor(int idSensor) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        return sensor;
    }
}
