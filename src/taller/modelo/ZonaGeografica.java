package taller.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.NivelVulnerabilidad;
import taller.enumeraciones.TipoEventoClimatico;
import taller.excepciones.UmbralInvalidoException;

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

    public ZonaGeografica() {
        this.idZona = generadorId++;
        this.nivelAlertaActual = NivelAlerta.VERDE;
        this.valoresHistoricos = new ArrayList<>();
        this.estacionesMeteorologicas = new ArrayList<>();
        this.alertas = new ArrayList<>();

    }

    public ZonaGeografica(String nombre, NivelVulnerabilidad nivelVulnerabilidad,
            NivelAlerta nivelAlertaActual) {
        this.idZona = generadorId++;
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

    public void agregarEstacionMeteorologica(EstacionMeteorologica estacion) {
        this.estacionesMeteorologicas.add(estacion);
        estacion.setZonaGeografica(this);
    }

    public void eliminarEstacionMeteorologica(EstacionMeteorologica estacion) {
        this.estacionesMeteorologicas.remove(estacion);
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }

    public void agregarAlerta(Alerta alerta) {
        this.alertas.add(alerta);
    }

    public void configurarUmbrales(double umbralAmarilla, double umbralNaranja, double umbralRoja)
            throws UmbralInvalidoException {
        if (!(0 < umbralAmarilla && umbralAmarilla < umbralNaranja && umbralNaranja < umbralRoja)) {
            throw new UmbralInvalidoException("Los umbrales deben cumplir 0 < amarilla < naranja < roja.");
        }
        this.umbralAmarilla = umbralAmarilla;
        this.umbralNaranja = umbralNaranja;
        this.umbralRoja = umbralRoja;
    }

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

    public Alerta actualizarNivelAlerta(LocalDateTime referencia, String descripcion, EquipoRespuesta equipoRespuesta) {
        NivelAlerta nivelAnterior = nivelAlertaActual;
        nivelAlertaActual = evaluarRiesgo(referencia);

        if (nivelAlertaActual.ordinal() > nivelAnterior.ordinal()) {
            return emitirAlerta(TipoEventoClimatico.LLUVIA_COPIOSA, nivelAlertaActual,
                    referencia, descripcion, equipoRespuesta);
        }
        return null;
    }

    public Alerta emitirAlerta(TipoEventoClimatico tipoEvento, NivelAlerta nivel, LocalDateTime fechaHora,
            String descripcion, EquipoRespuesta equipoRespuesta) {
        Alerta alerta = new Alerta(fechaHora, nivel, tipoEvento, descripcion, equipoRespuesta, this);
        agregarAlerta(alerta);
        return alerta;
    }

    public ValorHistorico obtenerValorHistorico(int mes) {
        for (ValorHistorico valor : valoresHistoricos) {
            if (valor.getMes() == mes) {
                return valor;
            }
        }
        return null;
    }

    public double acumuladoMensual(int anio, int mes) {
        LocalDateTime inicioMes = LocalDateTime.of(anio, mes, 1, 0, 0);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusNanos(1);
        double total = 0.0;
        for (EstacionMeteorologica estacion : estacionesMeteorologicas) {
            total += estacion.acumuladoEntreSensores(inicioMes, finMes);
        }
        return total;
    }

    public double desviacionMensual(int anio, int mes) {
        ValorHistorico historico = obtenerValorHistorico(mes);
        if (historico == null || historico.getAcumuladoMedio() == 0) {
            throw new IllegalStateException("La zona no tiene valor histórico cargado para el mes " + mes + ".");
        }
        double acumulado = acumuladoMensual(anio, mes);
        return (acumulado - historico.getAcumuladoMedio()) / historico.getAcumuladoMedio() * 100;
    }

    public String toString() {
        return "ZonaGeografica [idZona=" + idZona + ", nombre=" + nombre + ", nivelVulnerabilidad="
                + nivelVulnerabilidad + ", nivelAlertaActual=" + nivelAlertaActual + "]";
    }
}
