package taller.excepciones;

/**
 * Se lanza al asignar a una alerta un equipo de respuesta que no tiene integrantes.
 */
public class EquipoSinIntegrantesException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje que describe el error.
     *
     * @param mensaje descripcion del error
     */
    public EquipoSinIntegrantesException(String mensaje) {
        super(mensaje);
    }
}
