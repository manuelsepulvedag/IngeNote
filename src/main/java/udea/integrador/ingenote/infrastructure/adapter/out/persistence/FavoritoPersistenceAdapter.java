package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.FavoritoPersistencePort;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.FavoritoJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.FavoritoSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.UsuarioSpringDataRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class FavoritoPersistenceAdapter implements FavoritoPersistencePort {
    private final FavoritoSpringDataRepository favoritos;
    private final UsuarioSpringDataRepository usuarios;

    public FavoritoPersistenceAdapter(FavoritoSpringDataRepository favoritos, UsuarioSpringDataRepository usuarios) {
        this.favoritos = favoritos;
        this.usuarios = usuarios;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar(UUID idUsuario) {
        return favoritos.findByUsuarioIdOrderByFechaCreacionAsc(idUsuario).stream()
                .map(f -> UsuarioPersistenceMapper.aDominio(f.getFavorito()))
                .toList();
    }

    @Override
    @Transactional
    public void agregar(UUID idUsuario, UUID idFavorito) {
        if (favoritos.existsByUsuarioIdAndFavoritoId(idUsuario, idFavorito)) return;
        favoritos.save(new FavoritoJpaEntity(usuarios.getReferenceById(idUsuario),
                usuarios.getReferenceById(idFavorito), LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void quitar(UUID idUsuario, UUID idFavorito) {
        favoritos.eliminar(idUsuario, idFavorito);
    }
}
