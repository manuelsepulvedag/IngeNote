package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/** Hilo del foro. idRespuestaSolucion es null mientras el autor no marque una respuesta. */
public record ForoHilo(UUID id, Asignatura asignatura, Usuario autor, String titulo,
                       String contenido, LocalDateTime fechaCreacion, UUID idRespuestaSolucion) { }
