package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/** Modelo de negocio; no conoce Spring ni persistencia. */
public record Recurso(
        UUID id,
        Usuario usuario,
        Asignatura asignatura,
        TipoFormato tipoFormato,
        String titulo,
        String urlStorage,
        LocalDateTime fechaSubida
) {
}