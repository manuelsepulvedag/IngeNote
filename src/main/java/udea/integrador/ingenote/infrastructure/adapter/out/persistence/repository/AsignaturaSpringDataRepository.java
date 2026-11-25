package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.AsignaturaJpaEntity;

import java.util.UUID;

public interface AsignaturaSpringDataRepository extends JpaRepository<AsignaturaJpaEntity, UUID> {
}