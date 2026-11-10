package taller.excepciones;

public class EstacionFueraDeServicioException extends RuntimeException {
    public EstacionFueraDeServicioException(String mensaje) {
        super(mensaje);
    }
}