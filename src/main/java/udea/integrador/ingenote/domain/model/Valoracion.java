package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Valoracion(UUID id, UUID recursoId, UUID usuarioId, short voto,
                         LocalDateTime fechaCreacion) { }
