package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.ValoracionJpaEntity;
import java.util.Optional;
import java.util.UUID;

public interface ValoracionSpringDataRepository extends JpaRepository<ValoracionJpaEntity, UUID> {
    Optional<ValoracionJpaEntity> findByRecursoIdAndUsuarioId(UUID recursoId, UUID usuarioId);

    /** La columna "voto" guarda las estrellas (1 a 5). Devuelve null si no hay valoraciones. */
    @Query("select avg(v.voto) from ValoracionJpaEntity v where v.recurso.id = :id")
    Double promedio(@Param("id") UUID recursoId);

    long countByRecursoId(UUID recursoId);

    @Modifying
    @Query("delete from ValoracionJpaEntity v where v.recurso.id = :id")
    void deleteByRecursoId(@Param("id") UUID recursoId);
}
