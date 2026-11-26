package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/** Valoración de un recurso por un usuario, de 1 a 5 estrellas. */
public record Valoracion(UUID id, UUID recursoId, UUID usuarioId, short estrellas,
                         LocalDateTime fechaCreacion) {

    public static final short MIN_ESTRELLAS = 1;
    public static final short MAX_ESTRELLAS = 5;

    public static boolean esValida(short estrellas) {
        return estrellas >= MIN_ESTRELLAS && estrellas <= MAX_ESTRELLAS;
    }
}
