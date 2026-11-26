package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Recurso;
import java.util.UUID;

public interface EditarRecursoUseCase {
    /**
     * Actualiza título, asignatura y descripción. Si el comando trae un archivo
     * o un enlace, reemplaza el adjunto; si no, conserva el actual.
     * Solo el autor o un administrador.
     */
    Recurso editar(UUID recursoId, SubirRecursoCommand command);
}
