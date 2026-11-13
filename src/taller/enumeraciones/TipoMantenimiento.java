package taller.enumeraciones;

/**
 * Tipo de mantenimiento realizado sobre una estacion o un sensor.
 */
public enum TipoMantenimiento {
    /** Mantenimiento preventivo. */
    PREVENTIVO,
    /** Reparacion de una falla. */
    CORRECTIVO,
    /** Calibracion de un sensor, actualiza su fecha de ultima calibracion. */
    CALIBRACION
}
