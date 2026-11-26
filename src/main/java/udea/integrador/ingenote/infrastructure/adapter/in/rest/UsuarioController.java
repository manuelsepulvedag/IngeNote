package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import udea.integrador.ingenote.application.port.in.CuentaUseCase;
import udea.integrador.ingenote.application.port.in.FavoritoUseCase;
import java.util.List;
import java.util.UUID;

/** Cuenta y favoritos del usuario autenticado. */
@RestController
@RequestMapping("/api/usuarios/me")
public class UsuarioController {
    private final CuentaUseCase cuenta;
    private final FavoritoUseCase favoritos;

    public UsuarioController(CuentaUseCase cuenta, FavoritoUseCase favoritos) {
        this.cuenta = cuenta;
        this.favoritos = favoritos;
    }

    record PerfilRequest(@NotBlank @Size(max = 150) String nombre, UUID idCarrera) { }
    record ContrasenaRequest(@NotBlank String actual, @NotBlank @Size(min = 8, max = 72) String nueva) { }
    record FavoritoResponse(UUID id, String nombre, UUID idCarrera) { }

    @PutMapping
    public AuthController.UsuarioResponse actualizarPerfil(@Valid @RequestBody PerfilRequest r, Authentication a) {
        return AuthController.UsuarioResponse.de(cuenta.actualizarPerfil(a.getName(), r.nombre(), r.idCarrera()));
    }

    @PutMapping("/contrasena")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarContrasena(@Valid @RequestBody ContrasenaRequest r, Authentication a) {
        cuenta.cambiarContrasena(a.getName(), r.actual(), r.nueva());
    }

    /** Elimina la cuenta junto con sus recursos, hilos, respuestas, valoraciones y favoritos. */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCuenta(Authentication a) {
        cuenta.eliminarCuenta(a.getName());
    }

    @GetMapping("/favoritos")
    public List<FavoritoResponse> favoritos(Authentication a) {
        return favoritos.listar(a.getName()).stream()
                .map(u -> new FavoritoResponse(u.id(), u.nombre(), u.idCarrera()))
                .toList();
    }

    @PutMapping("/favoritos/{idUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void agregarFavorito(@PathVariable UUID idUsuario, Authentication a) {
        favoritos.agregar(a.getName(), idUsuario);
    }

    @DeleteMapping("/favoritos/{idUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quitarFavorito(@PathVariable UUID idUsuario, Authentication a) {
        favoritos.quitar(a.getName(), idUsuario);
    }
}
