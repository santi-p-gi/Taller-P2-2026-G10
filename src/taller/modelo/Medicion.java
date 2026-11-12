package taller.modelo;

import java.time.LocalDateTime;

/**
 * Lectura de precipitacion registrada por un sensor. Forma parte del historial del sensor (composicion).
 */
public class Medicion {
    private static int generadorId;
    private final int idMedicion;
    private LocalDateTime fechaHora;
    private double milimetros;
    private boolean anomala;

    /**
     * Crea una medicion vacia con ID autogenerado.
     */
    public Medicion() {
        this.idMedicion = ++generadorId;
        this.anomala = false;
    }

    /**
     * Crea una medicion con ID autogenerado.
     *
     * @param fechaHora  fecha y hora de la lectura
     * @param milimetros milimetros registrados
     * @param anomala    true si difiere en mas de 100 mm de la lectura anterior del sensor
     */
    public Medicion(LocalDateTime fechaHora, double milimetros, boolean anomala) {
        this.idMedicion = ++generadorId;
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

    public String toString() {
        return "Medicion [idMedicion=" + idMedicion + ", fechaHora=" + fechaHora + ", milimetros=" + milimetros
                + ", anomala=" + anomala + "]";
    }
}
