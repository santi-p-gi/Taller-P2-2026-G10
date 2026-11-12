package taller.excepciones;

/**
 * Se lanza al registrar una lectura en un sensor cuya estacion no esta operativa.
 */
public class EstacionFueraDeServicioException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje que describe el error.
     *
     * @param mensaje descripcion del error
     */
    public EstacionFueraDeServicioException(String mensaje) {
        super(mensaje);
    }
}
