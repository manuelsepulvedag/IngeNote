package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.FavoritoJpaEntity;
import java.util.List;
import java.util.UUID;

public interface FavoritoSpringDataRepository extends JpaRepository<FavoritoJpaEntity, UUID> {
    List<FavoritoJpaEntity> findByUsuarioIdOrderByFechaCreacionAsc(UUID idUsuario);

    boolean existsByUsuarioIdAndFavoritoId(UUID idUsuario, UUID idFavorito);

    @Modifying
    @Query("delete from FavoritoJpaEntity f where f.usuario.id = :usuario and f.favorito.id = :favorito")
    void eliminar(@Param("usuario") UUID idUsuario, @Param("favorito") UUID idFavorito);
}
