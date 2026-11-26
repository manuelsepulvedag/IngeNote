package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ForoRespuesta(UUID id, UUID hiloId, Usuario autor, String contenido,
                            LocalDateTime fechaCreacion) { }
