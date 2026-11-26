package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udea.integrador.ingenote.application.port.in.ConsultarPerfilUseCase;
import udea.integrador.ingenote.application.port.in.IniciarSesionCommand;
import udea.integrador.ingenote.application.port.in.IniciarSesionUseCase;
import udea.integrador.ingenote.application.port.in.RegistrarUsuarioUseCase;
import udea.integrador.ingenote.application.port.in.RegistroUsuarioCommand;
import udea.integrador.ingenote.domain.model.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegistrarUsuarioUseCase registrar;
    private final IniciarSesionUseCase iniciarSesion;
    private final ConsultarPerfilUseCase perfil;

    public AuthController(RegistrarUsuarioUseCase registrar, IniciarSesionUseCase iniciarSesion,
                          ConsultarPerfilUseCase perfil) {
        this.registrar = registrar;
        this.iniciarSesion = iniciarSesion;
        this.perfil = perfil;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        Usuario usuario = registrar.registrar(new RegistroUsuarioCommand(request.nombre(), request.correo(),
                request.contrasena(), request.idCarrera()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.de(usuario));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return new TokenResponse(iniciarSesion.iniciarSesion(new IniciarSesionCommand(request.correo(), request.contrasena())), "Bearer");
    }

    /** Datos del usuario dueño del token. */
    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        return UsuarioResponse.de(perfil.actual(authentication.getName()));
    }

    public record RegistroRequest(@NotBlank @Size(max = 150) String nombre, @NotBlank @Email @Size(max = 255) String correo,
                                  @NotBlank @Size(min = 8, max = 72) String contrasena, UUID idCarrera) { }
    public record LoginRequest(@NotBlank @Email String correo, @NotBlank String contrasena) { }
    public record UsuarioResponse(UUID id, String nombre, String correo, String rol, LocalDateTime fechaRegistro,
                                  UUID idCarrera) {
        static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.id(), u.nombre(), u.correo(), u.rol().nombre(), u.fechaRegistro(), u.idCarrera());
        }
    }
    public record TokenResponse(String accessToken, String tokenType) { }
}
