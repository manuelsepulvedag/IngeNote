package udea.integrador.ingenote.domain.model;

import java.util.List;
import java.util.UUID;

/** Carrera con las asignaturas que la componen. */
public record Carrera(UUID id, String nombre, List<Asignatura> asignaturas) {
}
