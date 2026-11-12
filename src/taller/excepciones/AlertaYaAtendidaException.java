package taller.excepciones;

/**
 * Se lanza al intentar marcar como atendida una alerta que ya lo estaba.
 */
public class AlertaYaAtendidaException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje que describe el error.
     *
     * @param mensaje descripcion del error
     */
    public AlertaYaAtendidaException(String mensaje) {
        super(mensaje);
    }
}
