package taller.modelo;

import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.NivelVulnerabilidad;

public class ZonaGeografica {
    private String idZona;
    private String nombre;
    private NivelVulnerabilidad nivelVulnerabilidad;
    private NivelAlerta nivelAlertaActual;
    private List<ValorHistorico> valoresHistoricos;
    private List<EstacionMeteorologica> estacionesMeteorologicas;
    private List<Alerta> alertas;

    public ZonaGeografica() {
        this.valoresHistoricos = new ArrayList<>();
        this.estacionesMeteorologicas = new ArrayList<>();
        this.alertas = new ArrayList<>();

    }

    public ZonaGeografica(String idZona, String nombre, NivelVulnerabilidad nivelVulnerabilidad,
            NivelAlerta nivelAlertaActual) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.nivelVulnerabilidad = nivelVulnerabilidad;
        this.nivelAlertaActual = nivelAlertaActual;
        this.valoresHistoricos = new ArrayList<>();
        this.estacionesMeteorologicas = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
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

}
