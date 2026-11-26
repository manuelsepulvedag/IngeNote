package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ForoHilo(UUID id, Asignatura asignatura, Usuario autor, String titulo,
                       String contenido, LocalDateTime fechaCreacion) { }
