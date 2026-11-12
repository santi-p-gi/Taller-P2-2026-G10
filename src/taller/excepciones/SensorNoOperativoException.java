package taller.excepciones;

/**
 * Se lanza al registrar una lectura en un sensor que no esta operativo.
 */
public class SensorNoOperativoException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje que describe el error.
     *
     * @param mensaje descripcion del error
     */
    public SensorNoOperativoException(String mensaje) {
        super(mensaje);
    }
}
