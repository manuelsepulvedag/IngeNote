package udea.integrador.ingenote.application.exception;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        super("Correo o contraseña inválidos");
    }
}
