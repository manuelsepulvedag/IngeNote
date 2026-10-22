package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RolJpaEntity;
import java.util.Optional;

public interface RolSpringDataRepository extends JpaRepository<RolJpaEntity, Integer> {
    Optional<RolJpaEntity> findByNombre(String nombre);
}
