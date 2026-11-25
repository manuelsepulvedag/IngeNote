package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RecursoJpaEntity;

import java.util.List;
import java.util.UUID;

public interface RecursoSpringDataRepository extends JpaRepository<RecursoJpaEntity, UUID> {
    List<RecursoJpaEntity> findAllByOrderByFechaSubidaDesc();
    List<RecursoJpaEntity> findByAsignaturaIdOrderByFechaSubidaDesc(UUID idAsignatura);
}