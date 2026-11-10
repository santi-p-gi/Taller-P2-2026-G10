package taller.excepciones;

public class UmbralInvalidoException extends RuntimeException {
    public UmbralInvalidoException(String mensaje) {
        super(mensaje);
    }
}