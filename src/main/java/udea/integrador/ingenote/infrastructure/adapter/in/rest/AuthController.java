package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udea.integrador.ingenote.application.port.in.IniciarSesionCommand;
import udea.integrador.ingenote.application.port.in.IniciarSesionUseCase;
import udea.integrador.ingenote.application.port.in.RegistrarUsuarioUseCase;
import udea.integrador.ingenote.application.port.in.RegistroUsuarioCommand;
import udea.integrador.ingenote.domain.model.Usuario;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegistrarUsuarioUseCase registrar;
    private final IniciarSesionUseCase iniciarSesion;
    public AuthController(RegistrarUsuarioUseCase registrar, IniciarSesionUseCase iniciarSesion) { this.registrar = registrar; this.iniciarSesion = iniciarSesion; }
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        Usuario usuario = registrar.registrar(new RegistroUsuarioCommand(request.nombre(), request.correo(), request.contrasena()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponse(usuario.id(), usuario.nombre(), usuario.correo(), usuario.rol().nombre()));
    }
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return new TokenResponse(iniciarSesion.iniciarSesion(new IniciarSesionCommand(request.correo(), request.contrasena())), "Bearer");
    }
    public record RegistroRequest(@NotBlank @Size(max = 150) String nombre, @NotBlank @Email @Size(max = 255) String correo,
                                  @NotBlank @Size(min = 8, max = 72) String contrasena) { }
    public record LoginRequest(@NotBlank @Email String correo, @NotBlank String contrasena) { }
    public record UsuarioResponse(UUID id, String nombre, String correo, String rol) { }
    public record TokenResponse(String accessToken, String tokenType) { }
}
