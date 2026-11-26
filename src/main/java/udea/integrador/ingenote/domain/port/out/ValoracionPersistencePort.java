package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Valoracion;
import java.util.Optional;
import java.util.UUID;

public interface ValoracionPersistencePort {
    boolean existeRecurso(UUID recursoId);
    Optional<Valoracion> buscarPorRecursoYUsuario(UUID recursoId, UUID usuarioId);
    Valoracion guardar(Valoracion valoracion);
    void eliminar(UUID id);
    long puntajeNeto(UUID recursoId);
    long cantidadVotos(UUID recursoId);
}
