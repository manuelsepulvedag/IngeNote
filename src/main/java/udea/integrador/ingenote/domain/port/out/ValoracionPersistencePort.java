package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Valoracion;
import java.util.Optional;
import java.util.UUID;

public interface ValoracionPersistencePort {
    /** Id del usuario que subió el recurso; vacío si el recurso no existe. */
    Optional<UUID> buscarAutorRecurso(UUID recursoId);
    Optional<Valoracion> buscarPorRecursoYUsuario(UUID recursoId, UUID usuarioId);
    Valoracion guardar(Valoracion valoracion);
    void eliminar(UUID id);
    /** Promedio de estrellas; 0 si el recurso no tiene valoraciones. */
    double promedio(UUID recursoId);
    long cantidadValoraciones(UUID recursoId);
}
