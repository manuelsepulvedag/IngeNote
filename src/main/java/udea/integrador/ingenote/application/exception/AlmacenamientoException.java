package udea.integrador.ingenote.application.exception;

public class AlmacenamientoException extends RuntimeException {
    public AlmacenamientoException(String mensaje) {
        super(mensaje);
    }

    public AlmacenamientoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}