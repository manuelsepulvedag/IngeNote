package udea.integrador.ingenote.domain.port.out;

public interface PasswordCipherPort {
    String cifrar(String contrasenaPlana);
    boolean coincide(String contrasenaPlana, String hash);
}
