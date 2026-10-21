package taller.modelo;

import java.time.LocalDateTime;

public class Medicion {
    private String idMedicion;
    private LocalDateTime fechaHora;
    private double milimetros;
    private boolean anomala;

    public Medicion() {
        this.anomala = false;
    }

    public Medicion(String idMedicion, LocalDateTime fechaHora, double milimetros, boolean anomala) {
        this.idMedicion = idMedicion;
        this.fechaHora = fechaHora;
        this.milimetros = milimetros;
        this.anomala = anomala;
    }

    public String getIdMedicion() {
        return idMedicion;
    }

    public void setIdMedicion(String idMedicion) {
        this.idMedicion = idMedicion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getMilimetros() {
        return milimetros;
    }

    public void setMilimetros(double milimetros) {
        this.milimetros = milimetros;
    }

    public boolean isAnomala() {
        return anomala;
    }

    public void setAnomala(boolean anomala) {
        this.anomala = anomala;
    }
}
