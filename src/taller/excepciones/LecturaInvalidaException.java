package taller.excepciones;

public class LecturaInvalidaException extends RuntimeException {

    public LecturaInvalidaException(String mensaje) {
        super(mensaje);
    }
}