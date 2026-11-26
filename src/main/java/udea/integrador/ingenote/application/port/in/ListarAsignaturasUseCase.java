package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Asignatura;
import java.util.List;

public interface ListarAsignaturasUseCase {
    /** Asignaturas ordenadas por semestre y nombre. */
    List<Asignatura> listar();
}
