package taller.modelo;

import java.time.LocalDateTime;

public class Medicion {
    private static int generadorId;
    private final int idMedicion;
    private LocalDateTime fechaHora;
    private double milimetros;
    private boolean anomala;

    public Medicion() {
        this.idMedicion = generadorId++;
        this.anomala = false;
    }

    public Medicion(LocalDateTime fechaHora, double milimetros, boolean anomala) {
        this.idMedicion = generadorId++;
        this.fechaHora = fechaHora;
        this.milimetros = milimetros;
        this.anomala = anomala;
    }

    public int getIdMedicion() {
        return idMedicion;
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
