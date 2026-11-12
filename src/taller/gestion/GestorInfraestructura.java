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

/**
 * Gestion de la infraestructura meteorologica: zonas, estaciones y sensores.
 */
public class GestorInfraestructura {
    private RegistroDatos registro;

    /**
     * Crea el gestor sobre el registro de datos del sistema.
     *
     * @param registro registro de datos compartido
     */
    public GestorInfraestructura(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- Zonas geográficas ----------

    /**
     * Registra una zona nueva con nivel de alerta VERDE y los umbrales por defecto.
     *
     * @param nombre              nombre de la zona
     * @param nivelVulnerabilidad nivel de vulnerabilidad climatica
     * @return la zona registrada
     */
    public ZonaGeografica registrarZona(String nombre, NivelVulnerabilidad nivelVulnerabilidad) {
        ZonaGeografica nuevaZona = new ZonaGeografica(nombre, nivelVulnerabilidad,
                taller.enumeraciones.NivelAlerta.VERDE);
        registro.agregarZona(nuevaZona);
        return nuevaZona;
    }

    /**
     * Carga el valor historico de un mes para una zona. Si ese mes ya tenia valor, lo reemplaza.
     *
     * @param idZona         ID de la zona
     * @param mes            mes del anio (1 a 12)
     * @param acumuladoMedio acumulado medio del mes, en mm
     */
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

    /**
     * Registra una estacion operativa, la agrega a su zona y la pone a cargo de un responsable.
     *
     * @param direccion      direccion
     * @param nombre         nombre
     * @param latitud        latitud
     * @param longitud       longitud
     * @param altitud        altitud en metros
     * @param tipo           tipo de estacion
     * @param idZona         ID de la zona a la que pertenece
     * @param dniResponsable DNI del responsable tecnico a cargo
     * @return la estacion registrada
     */
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

    /**
     * Cambia el estado operativo de una estacion.
     *
     * @param idEstacion  ID de la estacion
     * @param nuevoEstado nuevo estado
     */
    public void cambiarEstadoEstacion(int idEstacion, EstadoOperativo nuevoEstado) {
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        estacion.setEstado(nuevoEstado);
    }

    // ---------- Sensores ----------

    /**
     * Registra un sensor operativo y sin instalar.
     *
     * @param marca             marca
     * @param modelo            modelo
     * @param precision         precision de medicion, en mm
     * @param ultimaCalibracion fecha de la ultima calibracion
     * @return el sensor registrado
     */
    public Sensor registrarSensor(String marca, String modelo, double precision, LocalDateTime ultimaCalibracion) {
        Sensor nuevoSensor = new Sensor(marca, modelo, precision, EstadoOperativo.OPERATIVA, ultimaCalibracion, null);
        registro.agregarSensor(nuevoSensor);
        return nuevoSensor;
    }

    /**
     * Instala un sensor en una estacion.
     *
     * @param idSensor   ID del sensor
     * @param idEstacion ID de la estacion
     * @throws IllegalStateException si el sensor ya esta instalado en otra estacion
     */
    public void instalarSensor(int idSensor, int idEstacion) {
        Sensor sensor = buscarSensorPorId(idSensor);
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        estacion.instalarSensor(sensor);
    }

    /**
     * Retira un sensor de una estacion.
     *
     * @param idSensor   ID del sensor
     * @param idEstacion ID de la estacion
     * @throws IllegalArgumentException si el sensor no esta instalado en esa estacion
     */
    public void retirarSensor(int idSensor, int idEstacion) {
        Sensor sensor = buscarSensorPorId(idSensor);
        EstacionMeteorologica estacion = buscarEstacionPorId(idEstacion);
        if (!estacion.getSensores().contains(sensor)) {
            throw new IllegalArgumentException(
                    "El sensor " + idSensor + " no esta instalado en la estacion " + idEstacion);
        }
        estacion.retirarSensor(sensor);
    }

    /**
     * Traslada un sensor instalado desde su estacion actual a otra.
     *
     * @param idSensor          ID del sensor
     * @param idEstacionDestino ID de la estacion destino
     * @throws IllegalArgumentException si el sensor no esta instalado o ya esta en la estacion destino
     */
    public void trasladarSensor(int idSensor, int idEstacionDestino) {
        Sensor sensor = buscarSensorPorId(idSensor);
        EstacionMeteorologica estacionDestino = buscarEstacionPorId(idEstacionDestino);
        EstacionMeteorologica estacionOrigen = sensor.getEstacionMeteorologica();
        if (estacionOrigen == null) {
            throw new IllegalArgumentException("El sensor no esta instalado en ninguna estacion.");
        }
        if (estacionOrigen == estacionDestino) {
            throw new IllegalArgumentException("El sensor ya esta instalado en la estacion de destino.");
        }

        estacionOrigen.retirarSensor(sensor);
        estacionDestino.instalarSensor(sensor);
    }

    /**
     * Cambia el estado operativo de un sensor.
     *
     * @param idSensor    ID del sensor
     * @param nuevoEstado nuevo estado
     */
    public void cambiarEstadoSensor(int idSensor, EstadoOperativo nuevoEstado) {
        Sensor sensor = buscarSensorPorId(idSensor);
        sensor.setEstado(nuevoEstado);
    }

    // ---------- Consultas ----------

    /**
     * Busca una zona por su ID.
     *
     * @param idZona ID de la zona
     * @return la zona
     * @throws IllegalArgumentException si no existe
     */
    public ZonaGeografica buscarZonaPorId(int idZona) {
        ZonaGeografica zona = registro.buscarZonaPorId(idZona);
        if (zona == null) {
            throw new IllegalArgumentException("No existe una zona con ID " + idZona);
        }
        return zona;
    }

    /**
     * Busca una estacion por su ID.
     *
     * @param idEstacion ID de la estacion
     * @return la estacion
     * @throws IllegalArgumentException si no existe
     */
    public EstacionMeteorologica buscarEstacionPorId(int idEstacion) {
        EstacionMeteorologica estacion = registro.buscarEstacionPorId(idEstacion);
        if (estacion == null) {
            throw new IllegalArgumentException("No existe una estacion con ID " + idEstacion);
        }
        return estacion;
    }

    /**
     * Busca un sensor por su ID.
     *
     * @param idSensor ID del sensor
     * @return el sensor
     * @throws IllegalArgumentException si no existe
     */
    public Sensor buscarSensorPorId(int idSensor) {
        Sensor sensor = registro.buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID " + idSensor);
        }
        return sensor;
    }

    /**
     * Devuelve todas las zonas.
     *
     * @return lista de zonas
     */
    public List<ZonaGeografica> listarZonas() {
        return registro.getZonasGeograficas();
    }

    /**
     * Devuelve todas las estaciones.
     *
     * @return lista de estaciones
     */
    public List<EstacionMeteorologica> listarEstaciones() {
        return registro.getEstacionesMeteorologicas();
    }

    /**
     * Devuelve las estaciones de una zona.
     *
     * @param idZona ID de la zona
     * @return estaciones de la zona
     */
    public List<EstacionMeteorologica> listarEstacionesPorZona(int idZona) {
        ZonaGeografica zona = buscarZona(idZona);
        return zona.getEstacionesMeteorologicas();
    }

    /**
     * Devuelve todos los sensores, instalados o no.
     *
     * @return lista de sensores
     */
    public List<Sensor> listarSensores() {
        return registro.getSensores();
    }

    /**
     * Devuelve los sensores que no estan instalados en ninguna estacion.
     *
     * @return sensores sin instalar
     */
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
