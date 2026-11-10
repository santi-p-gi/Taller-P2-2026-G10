package taller.excepciones;

public class SensorNoOperativoException extends RuntimeException {
    public SensorNoOperativoException(String mensaje) {
        super(mensaje);
    }
}