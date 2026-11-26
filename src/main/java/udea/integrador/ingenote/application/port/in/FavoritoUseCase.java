package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Usuario;
import java.util.List;
import java.util.UUID;

public interface FavoritoUseCase {
    List<Usuario> listar(String correo);
    void agregar(String correo, UUID idFavorito);
    void quitar(String correo, UUID idFavorito);
}
