package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Rol;
import udea.integrador.ingenote.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPersistencePort {
    boolean existePorCorreo(String correo);
    Optional<Usuario> buscarPorCorreo(String correo);
    Optional<Rol> buscarRolPorNombre(String nombre);
    Usuario guardar(Usuario usuario);
}
