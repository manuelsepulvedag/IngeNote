package udea.integrador.ingenote.application.port.in;

import java.util.UUID;

public interface EliminarRecursoUseCase {
    /** Elimina el recurso y sus valoraciones. Solo el autor o un administrador. */
    void eliminar(String correo, UUID recursoId);
}
