package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Carrera;
import java.util.List;

public interface ListarCarrerasUseCase {
    /** Carreras ordenadas por nombre, con sus asignaturas ordenadas por semestre y nombre. */
    List<Carrera> listar();
}
