package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Usuario;
import java.util.UUID;

/** Gestión de la cuenta del usuario autenticado. */
public interface CuentaUseCase {
    Usuario actualizarPerfil(String correo, String nombre, UUID idCarrera);
    void cambiarContrasena(String correo, String actual, String nueva);
    void eliminarCuenta(String correo);
}
