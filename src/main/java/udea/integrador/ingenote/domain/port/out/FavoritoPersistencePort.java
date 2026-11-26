package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Usuario;
import java.util.List;
import java.util.UUID;

/** Usuarios que un estudiante sigue como favoritos. */
public interface FavoritoPersistencePort {
    List<Usuario> listar(UUID idUsuario);
    /** No hace nada si ya era favorito. */
    void agregar(UUID idUsuario, UUID idFavorito);
    void quitar(UUID idUsuario, UUID idFavorito);
}
