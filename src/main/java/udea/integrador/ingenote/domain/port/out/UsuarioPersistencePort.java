package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Rol;
import udea.integrador.ingenote.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioPersistencePort {
    boolean existePorCorreo(String correo);
    Optional<Usuario> buscarPorCorreo(String correo);
    Optional<Rol> buscarRolPorNombre(String nombre);
    Usuario guardar(Usuario usuario);
    boolean existeCarrera(UUID idCarrera);
    Optional<Usuario> buscarPorId(UUID id);
    /** Actualiza nombre, carrera y resumen de contraseña de un usuario existente. */
    Usuario actualizar(Usuario usuario);
    /** Elimina la cuenta; la base de datos borra en cascada sus contenidos. */
    void eliminar(UUID id);
}
