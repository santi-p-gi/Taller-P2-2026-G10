package taller.excepciones;

public class AlertaYaAtendidaException extends RuntimeException {
    public AlertaYaAtendidaException(String mensaje) {
        super(mensaje);
    }
}