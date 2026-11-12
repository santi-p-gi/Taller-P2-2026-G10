package taller.excepciones;

/**
 * Se lanza cuando una lectura de precipitacion esta fuera del rango admitido (0 a 500 mm).
 */
public class LecturaInvalidaException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje que describe el error.
     *
     * @param mensaje descripcion del error
     */
    public LecturaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
