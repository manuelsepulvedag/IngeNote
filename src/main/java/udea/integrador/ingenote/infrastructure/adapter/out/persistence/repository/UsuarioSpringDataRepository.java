package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioSpringDataRepository extends JpaRepository<UsuarioJpaEntity, UUID> {
    boolean existsByCorreo(String correo);
    Optional<UsuarioJpaEntity> findByCorreo(String correo);
}
