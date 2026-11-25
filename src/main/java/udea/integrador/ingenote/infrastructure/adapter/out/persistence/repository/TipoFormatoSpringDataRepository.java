package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.TipoFormatoJpaEntity;

import java.util.Optional;

public interface TipoFormatoSpringDataRepository extends JpaRepository<TipoFormatoJpaEntity, Integer> {
    Optional<TipoFormatoJpaEntity> findByNombre(String nombre);
}