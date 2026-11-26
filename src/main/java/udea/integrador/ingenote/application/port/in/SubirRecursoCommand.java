package udea.integrador.ingenote.application.port.in;

import java.util.UUID;

/**
 * Datos para crear o editar un recurso. Se envía un archivo (nombreArchivo,
 * contentType, contenido) o un enlace externo; descripcion es opcional.
 */
public record SubirRecursoCommand(
        String correoUsuario,
        UUID idAsignatura,
        String titulo,
        String nombreArchivo,
        String contentType,
        byte[] contenido,
        String descripcion,
        String enlace
) {
    public boolean traeArchivo() {
        return contenido != null && contenido.length > 0;
    }

    public boolean traeEnlace() {
        return enlace != null && !enlace.isBlank();
    }
}
