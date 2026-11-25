package udea.integrador.ingenote.application.port.in;

import java.util.UUID;

public record SubirRecursoCommand(
        String correoUsuario,
        UUID idAsignatura,
        String titulo,
        String nombreArchivo,
        String contentType,
        byte[] contenido
) {
}