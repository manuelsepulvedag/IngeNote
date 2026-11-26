package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.CuentaUseCase;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.PasswordCipherPort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import java.util.UUID;

public class CuentaService implements CuentaUseCase {
    private final UsuarioPersistencePort usuarios;
    private final PasswordCipherPort passwordCipher;

    public CuentaService(UsuarioPersistencePort usuarios, PasswordCipherPort passwordCipher) {
        this.usuarios = usuarios;
        this.passwordCipher = passwordCipher;
    }

    @Override
    public Usuario actualizarPerfil(String correo, String nombre, UUID idCarrera) {
        Usuario u = usuario(correo);
        if (nombre == null || nombre.isBlank()) throw new ReglaDeNegocioException("El nombre es obligatorio");
        if (idCarrera != null && !usuarios.existeCarrera(idCarrera)) {
            throw new ReglaDeNegocioException("La carrera seleccionada no existe");
        }
        return usuarios.actualizar(new Usuario(u.id(), u.rol(), nombre.trim(), u.correo(), u.contrasenaHash(),
                u.fechaRegistro(), idCarrera));
    }

    @Override
    public void cambiarContrasena(String correo, String actual, String nueva) {
        Usuario u = usuario(correo);
        if (actual == null || !passwordCipher.coincide(actual, u.contrasenaHash())) {
            throw new ReglaDeNegocioException("La contraseña actual no es correcta");
        }
        // 72 es la longitud máxima que procesa BCrypt.
        if (nueva == null || nueva.length() < 8 || nueva.length() > 72) {
            throw new ReglaDeNegocioException("La nueva contraseña debe tener entre 8 y 72 caracteres");
        }
        usuarios.actualizar(new Usuario(u.id(), u.rol(), u.nombre(), u.correo(), passwordCipher.cifrar(nueva),
                u.fechaRegistro(), u.idCarrera()));
    }

    @Override
    public void eliminarCuenta(String correo) {
        usuarios.eliminar(usuario(correo).id());
    }

    private Usuario usuario(String correo) {
        return usuarios.buscarPorCorreo(correo)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado"));
    }
}
