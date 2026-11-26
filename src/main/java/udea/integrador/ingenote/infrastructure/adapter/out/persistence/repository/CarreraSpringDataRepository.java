package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.CarreraJpaEntity;

import java.util.List;
import java.util.UUID;

public interface CarreraSpringDataRepository extends JpaRepository<CarreraJpaEntity, UUID> {
    /** Carreras con sus asignaturas en una sola consulta. */
    @EntityGraph(attributePaths = "asignaturas")
    List<CarreraJpaEntity> findAllByOrderByNombreAsc();
}
