package taller.excepciones;

/**
 * Se lanza cuando los umbrales de una zona no cumplen 0 &lt; amarilla &lt; naranja &lt; roja.
 */
public class UmbralInvalidoException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje que describe el error.
     *
     * @param mensaje descripcion del error
     */
    public UmbralInvalidoException(String mensaje) {
        super(mensaje);
    }
}
