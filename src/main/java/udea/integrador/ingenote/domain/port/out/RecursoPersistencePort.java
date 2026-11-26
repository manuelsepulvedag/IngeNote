package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.Carrera;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.TipoFormato;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecursoPersistencePort {
    Optional<Asignatura> buscarAsignaturaPorId(UUID idAsignatura);
    Optional<TipoFormato> buscarTipoFormatoPorNombre(String nombre);
    Recurso guardar(Recurso recurso);
    List<Recurso> listarTodos();
    List<Recurso> listarPorAsignatura(UUID idAsignatura);
    List<Asignatura> listarAsignaturas();
    List<Carrera> listarCarreras();
    Optional<Recurso> buscarPorId(UUID id);
    /** Elimina el recurso junto con sus valoraciones. */
    void eliminar(UUID id);
}