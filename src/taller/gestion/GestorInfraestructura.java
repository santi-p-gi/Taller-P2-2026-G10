package taller.gestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.NivelVulnerabilidad;
import taller.enumeraciones.TipoEstacion;
import taller.modelo.ZonaGeografica;
import taller.modelo.ValorHistorico;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.ResponsableTecnico;
import taller.enumeraciones.EstadoOperativo;
import taller.modelo.Sensor;

public class GestorInfraestructura {
    private RegistroDatos registro;

    public GestorInfraestructura(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- Zonas geográficas ----------
    public ZonaGeografica registrarZona(String nombre, NivelVulnerabilidad nivelVulnerabilidad) {
        ZonaGeografica nuevaZona = new ZonaGeografica(nombre, nivelVulnerabilidad,
                taller.enumeraciones.NivelAlerta.VERDE);
        registro.agregarZona(nuevaZona);
        return nuevaZona;
    }

    public void cargarValorHistorico(int idZona, int mes, double acumuladoMedio) {

        ZonaGeografica zona = buscarZona(idZona);
        ValorHistorico valor = zona.obtenerValorHistorico(mes);

        if (valor == null) {
            zona.agregarValorHistorico(mes, acumuladoMedio);
        } else {
            valor.setAcumuladoMedio(acumuladoMedio);
        }
    }

    // ---------- Estaciones meteorológicas ----------
    public EstacionMeteorologica registrarEstacionMeteorologica(String direccion, String nombre, double latitud,
            double longitud, double altitud, TipoEstacion tipo, int idZona, String dniResponsable) {
        ZonaGeografica zona = buscarZona(idZona);
        ResponsableTecnico responsable = buscarResponsable(dniResponsable);
        EstacionMeteorologica nuevaEstacion = new EstacionMeteorologica(direccion, nombre, latitud, longitud, altitud,
                EstadoOperativo.OPERATIVA, tipo, null, null);
        zona.agregarEstacionMeteorologica(nuevaEstacion);
        nuevaEstacion.asignarResponsableTecnico(responsable);
        registro.agregarEstacion(nuevaEstacion);

        return nuevaEstacion;
    }

    public void cambiarEstadoEstacion(int idEstacion, EstadoOperativo nuevoEstado) {
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        estacion.setEstado(nuevoEstado);
    }

    // ---------- Sensores ----------
    public Sensor registrarSensor(String marca, String modelo, double precision, LocalDateTime ultimaCalibracion) {
        Sensor nuevoSensor = new Sensor(marca, modelo, precision, EstadoOperativo.OPERATIVA, ultimaCalibracion, null);
        registro.agregarSensor(nuevoSensor);
        return nuevoSensor;
    }

    public void instalarSensor(int idSensor, int idEstacion) {
        Sensor sensor = buscarSensorPorId(idSensor);
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        estacion.instalarSensor(sensor);
    }

    public void retirarSensor(int idSensor, int idEstacion) {
        Sensor sensor = buscarSensorPorId(idSensor);
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        if (!estacion.getSensores().contains(sensor)) {
            throw new IllegalArgumentException(
                    "El sensor " + idSensor + " no está instalado en la estación " + idEstacion);
        }
        estacion.retirarSensor(sensor);
    }

    public void trasladarSensor(int idSensor, int idEstacionDestino) {
        Sensor sensor = buscarSensorPorId(idSensor);
        EstacionMeteorologica estacionDestino = buscarEstacionPorId(idEstacionDestino);
        EstacionMeteorologica estacionOrigen = sensor.getEstacionMeteorologica();
        if (estacionOrigen == null) {
            throw new IllegalArgumentException("El sensor no está instalado en ninguna estación.");
        }
        if (estacionOrigen == estacionDestino) {
            throw new IllegalArgumentException("El sensor ya está instalado en la estación de destino.");
        }

        estacionOrigen.retirarSensor(sensor);
        estacionDestino.instalarSensor(sensor);
    }

    public void cambiarEstadoSensor(int idSensor, EstadoOperativo nuevoEstado) {
        Sensor sensor = buscarSensorPorId(idSensor);
        sensor.setEstado(nuevoEstado);
    }

    // ---------- Consultas ----------
    public ZonaGeografica buscarZonaPorId(int idZona) {
        ZonaGeografica zona = registro.buscarZonaPorId(idZona);
        if (zona == null) {
            throw new IllegalArgumentException("No existe una zona con ID " + idZona);
        }
        return zona;
    }

    public EstacionMeteorologica buscarEstacionPorId(int idEstacion) {
        EstacionMeteorologica estacion = registro.buscarEstacionPorId(idEstacion);
        if (estacion == null) {
            throw new IllegalArgumentException("No existe una estación con ID " + idEstacion);
        }
        return estacion;
    }

    public Sensor buscarSensorPorId(int idSensor) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        return sensor;
    }

    public List<ZonaGeografica> listarZonas() {
        return registro.getZonasGeograficas();
    }

    public List<EstacionMeteorologica> listarEstaciones() {
        return registro.getEstacionesMeteorologicas();
    }

    public List<EstacionMeteorologica> listarEstacionesPorZona(int idZona) {
        ZonaGeografica zona = buscarZona(idZona);
        return zona.getEstacionesMeteorologicas();
    }

    public List<Sensor> listarSensores() {
        return registro.getSensores();
    }

    public List<Sensor> listarSensoresSinInstalar() {
        List<Sensor> sensoresSinInstalar = new ArrayList<>();
        for (Sensor sensor : registro.getSensores()) {
            if (sensor.getEstacionMeteorologica() == null) {
                sensoresSinInstalar.add(sensor);
            }
        }
        return sensoresSinInstalar;
    }

    // ---------- Auxiliares ----------

    private ZonaGeografica buscarZona(int idZona) {
        ZonaGeografica zona = registro.buscarZonaPorId(idZona);
        if (zona == null) {
            throw new IllegalArgumentException("No existe una zona con ID " + idZona);
        }
        return zona;
    }

    private ResponsableTecnico buscarResponsable(String dni) {
        ResponsableTecnico responsable = registro.buscarResponsablePorDni(dni);
        if (responsable == null) {
            throw new IllegalArgumentException("No existe un responsable con DNI " + dni);
        }
        return responsable;
    }

}