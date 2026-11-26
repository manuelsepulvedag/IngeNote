package udea.integrador.ingenote.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/** Modelo de negocio; no conoce Spring ni persistencia. idCarrera puede ser null. */
public record Usuario(UUID id, Rol rol, String nombre, String correo,
                      String contrasenaHash, LocalDateTime fechaRegistro, UUID idCarrera) {
}
