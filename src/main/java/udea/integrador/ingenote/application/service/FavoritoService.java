package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.exception.RecursoNoEncontradoException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.FavoritoUseCase;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.FavoritoPersistencePort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import java.util.List;
import java.util.UUID;

public class FavoritoService implements FavoritoUseCase {
    private final FavoritoPersistencePort favoritos;
    private final UsuarioPersistencePort usuarios;

    public FavoritoService(FavoritoPersistencePort favoritos, UsuarioPersistencePort usuarios) {
        this.favoritos = favoritos;
        this.usuarios = usuarios;
    }

    @Override
    public List<Usuario> listar(String correo) {
        return favoritos.listar(usuario(correo).id());
    }

    @Override
    public void agregar(String correo, UUID idFavorito) {
        Usuario u = usuario(correo);
        if (u.id().equals(idFavorito)) throw new ReglaDeNegocioException("No puede agregarse a sí mismo como favorito");
        if (usuarios.buscarPorId(idFavorito).isEmpty()) throw new RecursoNoEncontradoException("No existe el usuario");
        favoritos.agregar(u.id(), idFavorito);
    }

    @Override
    public void quitar(String correo, UUID idFavorito) {
        favoritos.quitar(usuario(correo).id(), idFavorito);
    }

    private Usuario usuario(String correo) {
        return usuarios.buscarPorCorreo(correo)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado"));
    }
}
