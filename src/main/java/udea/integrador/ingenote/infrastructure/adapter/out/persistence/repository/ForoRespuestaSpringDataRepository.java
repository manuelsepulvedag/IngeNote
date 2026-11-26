package udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository;
import org.springframework.data.jpa.repository.JpaRepository; import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.ForoRespuestaJpaEntity; import java.util.*;
public interface ForoRespuestaSpringDataRepository extends JpaRepository<ForoRespuestaJpaEntity,UUID>{ List<ForoRespuestaJpaEntity> findByHiloIdOrderByFechaCreacionAsc(UUID id); long countByHiloId(UUID id); void deleteByHiloId(UUID id); }
