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

    /** {@return el identificador de la medicion} */
    public int getIdMedicion() {
        return idMedicion;
    }

    /** {@return la fecha y hora de la medicion} */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Modifica la fecha y hora de la medicion.
     *
     * @param fechaHora el nuevo valor
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /** {@return la lluvia medida, en mm} */
    public double getMilimetros() {
        return milimetros;
    }

    /**
     * Modifica la lluvia medida, en mm.
     *
     * @param milimetros el nuevo valor
     */
    public void setMilimetros(double milimetros) {
        this.milimetros = milimetros;
    }

    /** {@return true si la medicion es anomala} */
    public boolean isAnomala() {
        return anomala;
    }

    /**
     * Indica si la medicion es anomala.
     *
     * @param anomala true si la medicion es anomala
     */
    public void setAnomala(boolean anomala) {
        this.anomala = anomala;
    }

    public String toString() {
        return "Medicion [idMedicion=" + idMedicion + ", fechaHora=" + fechaHora + ", milimetros=" + milimetros
                + ", anomala=" + anomala + "]";
    }
}
