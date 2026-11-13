package taller.enumeraciones;

/**
 * Estado operativo de una estacion meteorologica o de un sensor.
 */
public enum EstadoOperativo {
    /** En funcionamiento normal. */
    OPERATIVA,
    /** En mantenimiento, no registra lecturas. */
    EN_MANTENIMIENTO,
    /** Fuera de servicio, no registra lecturas. */
    FUERA_DE_SERVICIO
}
