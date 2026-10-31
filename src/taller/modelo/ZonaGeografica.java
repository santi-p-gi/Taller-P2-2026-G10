package taller.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.NivelVulnerabilidad;
import taller.enumeraciones.TipoEventoClimatico;

public class ZonaGeografica {
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

    public void agregarValorHistorico(int mes, double acumuladoMedio) {
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

    public double lluviaAcumulada24hs() {
        double total = 0.0;
        LocalDateTime limite24hs = LocalDateTime.now().minusHours(24);
        for (EstacionMeteorologica estacion : estacionesMeteorologicas) {
            if (estacion.estaOperativa()) {
                for (Sensor sensor : estacion.getSensores()) {
                    for (Medicion medicion : sensor.getMediciones()) {
                        if (medicion.getFechaHora().isAfter(limite24hs) && !medicion.isAnomala()) {
                            total += medicion.getMilimetros();
                        }
                    }
                }
            }
        }
        return total;
    }

    public NivelAlerta evaluarRiesgo() {
        double lluvia24hs = lluviaAcumulada24hs();
        if (lluvia24hs > 100) {
            return NivelAlerta.ROJA;
        } else if (lluvia24hs > 50) {
            return NivelAlerta.AMARILLA;
        } else {
            return NivelAlerta.VERDE;
        }
    }

    public Alerta emitirAlerta(TipoEventoClimatico tipoEvento, String descripcion, EquipoRespuesta equipoRespuesta) {
        NivelAlerta nivel = evaluarRiesgo();
        Alerta alerta = new Alerta(LocalDateTime.now(), nivel, tipoEvento, descripcion,
                equipoRespuesta, this);
        agregarAlerta(alerta);
        return alerta;
    }
}
