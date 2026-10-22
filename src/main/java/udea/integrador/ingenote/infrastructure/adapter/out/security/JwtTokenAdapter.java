package udea.integrador.ingenote.infrastructure.adapter.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.TokenPort;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenAdapter implements TokenPort {
    private final SecretKey key;
    private final long expiracionSegundos;
    public JwtTokenAdapter(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-seconds:3600}") long expiracionSegundos) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracionSegundos = expiracionSegundos;
    }
    public String generar(Usuario usuario) {
        Instant now = Instant.now();
        return Jwts.builder().subject(usuario.correo()).claim("rol", usuario.rol().nombre()).issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiracionSegundos))).signWith(key).compact();
    }
    public Claims validarYObtenerClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
