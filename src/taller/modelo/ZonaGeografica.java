package taller.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.NivelVulnerabilidad;
import taller.enumeraciones.TipoEventoClimatico;
import taller.excepciones.UmbralInvalidoException;

/**
 * Zona geografica o cuenca hidrografica. Agrupa estaciones, define sus umbrales de lluvia en 24 horas,
 * guarda su nivel de alerta vigente, sus valores historicos mensuales y las alertas que emite.
 * Los umbrales por defecto son 50, 100 y 150 mm.
 */
public class ZonaGeografica {
    private double umbralAmarilla = 50.0;
    private double umbralRoja = 150.0;
    private double umbralNaranja = 100.0;
    private static int generadorId;
    private final int idZona;
    private String nombre;
    private NivelVulnerabilidad nivelVulnerabilidad;
    private NivelAlerta nivelAlertaActual;
    private List<ValorHistorico> valoresHistoricos;
    private List<EstacionMeteorologica> estacionesMeteorologicas;
    private List<Alerta> alertas;

    /**
     * Crea una zona vacia en nivel VERDE, con ID autogenerado.
     */
    public ZonaGeografica() {
        this.idZona = ++generadorId;
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.valoresHistoricos = new ArrayList<>();
        this.estacionesMeteorologicas = new ArrayList<>();
        this.alertas = new ArrayList<>();

    }

    /**
     * Crea una zona con ID autogenerado.
     *
     * @param nombre              nombre de la zona
     * @param nivelVulnerabilidad nivel de vulnerabilidad climatica
     * @param nivelAlertaActual   nivel de alerta inicial
     */
    public ZonaGeografica(String nombre, NivelVulnerabilidad nivelVulnerabilidad,
            NivelAlerta nivelAlertaActual) {
        this.idZona = ++generadorId;
        this.nombre = nombre;
        this.nivelVulnerabilidad = nivelVulnerabilidad;
        this.nivelAlertaActual = nivelAlertaActual;
        this.valoresHistoricos = new ArrayList<>();
        this.estacionesMeteorologicas = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public int getIdZona() {
        return idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public NivelVulnerabilidad getNivelVulnerabilidad() {
        return nivelVulnerabilidad;
    }

    public void setNivelVulnerabilidad(NivelVulnerabilidad nivelVulnerabilidad) {
        this.nivelVulnerabilidad = nivelVulnerabilidad;
    }

    public NivelAlerta getNivelAlertaActual() {
        return nivelAlertaActual;
    }

    public void setNivelAlertaActual(NivelAlerta nivelAlertaActual) {
        this.nivelAlertaActual = nivelAlertaActual;
    }

    public List<ValorHistorico> getValoresHistoricos() {
        return valoresHistoricos;
    }

    public void setValoresHistoricos(List<ValorHistorico> valoresHistoricos) {
        this.valoresHistoricos = valoresHistoricos;
    }

    /**
     * Agrega un valor historico mensual a la zona.
     *
     * @param mes            mes del anio (1 a 12)
     * @param acumuladoMedio acumulado medio del mes, en mm
     * @throws IllegalArgumentException si el mes no esta entre 1 y 12
     */
    public void agregarValorHistorico(int mes, double acumuladoMedio) throws IllegalArgumentException {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        this.valoresHistoricos.add(new ValorHistorico(mes, acumuladoMedio));
    }

    public List<EstacionMeteorologica> getEstacionesMeteorologicas() {
        return estacionesMeteorologicas;
    }

    public void setEstacionesMeteorologicas(List<EstacionMeteorologica> estacionesMeteorologicas) {
        this.estacionesMeteorologicas = estacionesMeteorologicas;
    }

    /**
     * Agrega una estacion a la zona y le asigna esta zona.
     *
     * @param estacion estacion a agregar
     */
    public void agregarEstacionMeteorologica(EstacionMeteorologica estacion) {
        this.estacionesMeteorologicas.add(estacion);
        estacion.setZonaGeografica(this);
    }

    /**
     * Quita una estacion de la lista de estaciones de la zona.
     *
     * @param estacion estacion a quitar
     */
    public void eliminarEstacionMeteorologica(EstacionMeteorologica estacion) {
        this.estacionesMeteorologicas.remove(estacion);
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }

    /**
     * Agrega una alerta a la lista de alertas emitidas por la zona.
     *
     * @param alerta alerta a agregar
     */
    public void agregarAlerta(Alerta alerta) {
        this.alertas.add(alerta);
    }

    /**
     * Configura los tres umbrales de lluvia acumulada en 24 horas.
     *
     * @param umbralAmarilla mm a partir de los cuales el nivel es AMARILLA
     * @param umbralNaranja  mm a partir de los cuales el nivel es NARANJA
     * @param umbralRoja     mm a partir de los cuales el nivel es ROJA
     * @throws UmbralInvalidoException si no se cumple 0 &lt; amarilla &lt; naranja &lt; roja
     */
    public void configurarUmbrales(double umbralAmarilla, double umbralNaranja, double umbralRoja)
            throws UmbralInvalidoException {
        if (!(0 < umbralAmarilla && umbralAmarilla < umbralNaranja && umbralNaranja < umbralRoja)) {
            throw new UmbralInvalidoException("Los umbrales deben cumplir 0 < amarilla < naranja < roja.");
        }
        this.umbralAmarilla = umbralAmarilla;
        this.umbralNaranja = umbralNaranja;
        this.umbralRoja = umbralRoja;
    }

    /**
     * Suma la lluvia de las 24 horas previas a la referencia, considerando solo las estaciones operativas.
     *
     * @param referencia fin de la ventana de 24 horas
     * @return milimetros acumulados
     */
    public double lluviaAcumulada24hs(LocalDateTime referencia) {
        double total = 0.0;
        LocalDateTime limite24hs = referencia.minusHours(24);
        for (EstacionMeteorologica estacion : estacionesMeteorologicas) {
            if (estacion.estaOperativa()) {
                for (Sensor sensor : estacion.getSensores()) {
                    for (Medicion medicion : sensor.getMediciones()) {
                        if (medicion.getFechaHora().isAfter(limite24hs)
                                && !medicion.getFechaHora().isAfter(referencia)) {
                            total += medicion.getMilimetros();
                        }
                    }
                }
            }
        }
        return total;
    }

    /**
     * Calcula el nivel de alerta que corresponde segun la lluvia de las ultimas 24 horas y los umbrales.
     *
     * @param referencia momento a evaluar
     * @return nivel de alerta calculado
     */
    public NivelAlerta evaluarRiesgo(LocalDateTime referencia) {
        double lluvia24hs = lluviaAcumulada24hs(referencia);
        if (lluvia24hs >= umbralRoja) {
            return NivelAlerta.ROJA;
        } else if (lluvia24hs >= umbralNaranja) {
            return NivelAlerta.NARANJA;
        } else if (lluvia24hs >= umbralAmarilla) {
            return NivelAlerta.AMARILLA;
        } else {
            return NivelAlerta.VERDE;
        }

    }

    /**
     * Actualiza el nivel de alerta vigente. Si el nivel sube, emite una alerta LLUVIA_COPIOSA con el nuevo nivel.
     *
     * @param referencia      momento a evaluar
     * @param descripcion     descripcion de la alerta, si se emite
     * @param equipoRespuesta equipo a asignar a la alerta, o null
     * @return la alerta emitida, o null si el nivel no subio
     */
    public Alerta actualizarNivelAlerta(LocalDateTime referencia, String descripcion, EquipoRespuesta equipoRespuesta) {
        NivelAlerta nivelAnterior = nivelAlertaActual;
        nivelAlertaActual = evaluarRiesgo(referencia);

        if (nivelAlertaActual.ordinal() > nivelAnterior.ordinal()) {
            return emitirAlerta(TipoEventoClimatico.LLUVIA_COPIOSA, nivelAlertaActual,
                    referencia, descripcion, equipoRespuesta);
        }
        return null;
    }

    /**
     * Crea una alerta de esta zona y la agrega a su lista de alertas.
     *
     * @param tipoEvento      tipo de evento climatico
     * @param nivel           nivel de severidad
     * @param fechaHora       fecha y hora de emision
     * @param descripcion     descripcion de la alerta
     * @param equipoRespuesta equipo a asignar, o null
     * @return la alerta emitida
     */
    public Alerta emitirAlerta(TipoEventoClimatico tipoEvento, NivelAlerta nivel, LocalDateTime fechaHora,
            String descripcion, EquipoRespuesta equipoRespuesta) {
        Alerta alerta = new Alerta(fechaHora, nivel, tipoEvento, descripcion, equipoRespuesta, this);
        agregarAlerta(alerta);
        return alerta;
    }

    /**
     * Busca el valor historico de un mes.
     *
     * @param mes mes del anio (1 a 12)
     * @return el valor historico, o null si no fue cargado
     */
    public ValorHistorico obtenerValorHistorico(int mes) {
        for (ValorHistorico valor : valoresHistoricos) {
            if (valor.getMes() == mes) {
                return valor;
            }
        }
        return null;
    }

    /**
     * Suma la lluvia registrada en un mes por todas las estaciones de la zona.
     *
     * @param anio anio
     * @param mes  mes (1 a 12)
     * @return milimetros acumulados en el mes
     */
    public double acumuladoMensual(int anio, int mes) {
        LocalDateTime inicioMes = LocalDateTime.of(anio, mes, 1, 0, 0);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusNanos(1);
        double total = 0.0;
        for (EstacionMeteorologica estacion : estacionesMeteorologicas) {
            total += estacion.acumuladoEntreSensores(inicioMes, finMes);
        }
        return total;
    }

    /**
     * Calcula el porcentaje de desviacion del acumulado de un mes respecto de su valor historico.
     *
     * @param anio anio
     * @param mes  mes (1 a 12)
     * @return desviacion en porcentaje (positiva si llovio mas que lo historico)
     * @throws IllegalStateException si no hay valor historico cargado para el mes
     */
    public double desviacionMensual(int anio, int mes) {
        ValorHistorico historico = obtenerValorHistorico(mes);
        if (historico == null || historico.getAcumuladoMedio() == 0) {
            throw new IllegalStateException("La zona no tiene valor historico cargado para el mes " + mes + ".");
        }
        double acumulado = acumuladoMensual(anio, mes);
        return (acumulado - historico.getAcumuladoMedio()) / historico.getAcumuladoMedio() * 100;
    }

    public String toString() {
        return "ZonaGeografica [idZona=" + idZona + ", nombre=" + nombre + ", nivelVulnerabilidad="
                + nivelVulnerabilidad + ", nivelAlertaActual=" + nivelAlertaActual + "]";
    }
}
