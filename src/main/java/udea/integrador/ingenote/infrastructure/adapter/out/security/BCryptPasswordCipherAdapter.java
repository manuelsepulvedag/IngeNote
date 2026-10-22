package udea.integrador.ingenote.infrastructure.adapter.out.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import udea.integrador.ingenote.domain.port.out.PasswordCipherPort;

@Component
public class BCryptPasswordCipherAdapter implements PasswordCipherPort {
    private final PasswordEncoder passwordEncoder;
    public BCryptPasswordCipherAdapter(PasswordEncoder passwordEncoder) { this.passwordEncoder = passwordEncoder; }
    public String cifrar(String contrasenaPlana) { return passwordEncoder.encode(contrasenaPlana); }
    public boolean coincide(String contrasenaPlana, String hash) { return passwordEncoder.matches(contrasenaPlana, hash); }
}
