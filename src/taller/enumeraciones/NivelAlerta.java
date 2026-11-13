package taller.enumeraciones;

/**
 * Nivel de severidad de una alerta y nivel de alerta vigente de una zona, de menor a mayor.
 */
public enum NivelAlerta {
    /** Sin riesgo. */
    VERDE,
    /** Riesgo moderado. */
    AMARILLA,
    /** Riesgo alto, requiere equipo de respuesta. */
    NARANJA,
    /** Riesgo extremo, requiere equipo de respuesta. */
    ROJA
}
