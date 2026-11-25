package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.SubirRecursoCommand;
import udea.integrador.ingenote.application.port.in.SubirRecursoUseCase;
import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.TipoFormato;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.ArchivoStoragePort;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

public class SubirRecursoService implements SubirRecursoUseCase {
    private static final long TAMANO_MAXIMO_BYTES = 20L * 1024L * 1024L; // 20 MB
    private static final Set<String> EXTENSIONES_IMAGEN = Set.of("png", "jpg", "jpeg", "webp", "gif");

    private final UsuarioPersistencePort usuarios;
    private final RecursoPersistencePort recursos;
    private final ArchivoStoragePort storage;

    public SubirRecursoService(
            UsuarioPersistencePort usuarios,
            RecursoPersistencePort recursos,
            ArchivoStoragePort storage
    ) {
        this.usuarios = usuarios;
        this.recursos = recursos;
        this.storage = storage;
    }

    @Override
    public Recurso subir(SubirRecursoCommand command) {
        if (command.titulo() == null || command.titulo().isBlank()) {
            throw new ReglaDeNegocioException("El título del recurso es obligatorio");
        }
        if (command.idAsignatura() == null) {
            throw new ReglaDeNegocioException("La asignatura es obligatoria");
        }
        if (command.contenido() == null || command.contenido().length == 0) {
            throw new ReglaDeNegocioException("Debe adjuntar un archivo con contenido");
        }
        if (command.contenido().length > TAMANO_MAXIMO_BYTES) {
            throw new ReglaDeNegocioException("El archivo supera el tamaño máximo permitido de 20 MB");
        }

        String nombreFormato = detectarTipoFormato(command.contentType(), command.nombreArchivo());

        Usuario usuario = usuarios.buscarPorCorreo(command.correoUsuario().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ReglaDeNegocioException("No se encontró el usuario autenticado"));

        Asignatura asignatura = recursos.buscarAsignaturaPorId(command.idAsignatura())
                .orElseThrow(() -> new ReglaDeNegocioException("No existe la asignatura con id " + command.idAsignatura()));

        TipoFormato tipoFormato = recursos.buscarTipoFormatoPorNombre(nombreFormato)
                .orElseThrow(() -> new ReglaDeNegocioException("No existe el tipo de formato " + nombreFormato + " en la base de datos"));

        String contentTypeSeguro = (command.contentType() == null || command.contentType().isBlank())
                ? "application/octet-stream"
                : command.contentType();

        String carpeta = "asignatura-" + asignatura.id();
        String urlPublica = storage.subir(carpeta, command.nombreArchivo(), contentTypeSeguro, command.contenido());

        Recurso nuevoRecurso = new Recurso(
                null,
                usuario,
                asignatura,
                tipoFormato,
                command.titulo().trim(),
                urlPublica,
                LocalDateTime.now()
        );

        return recursos.guardar(nuevoRecurso);
    }

    private String detectarTipoFormato(String contentType, String nombreArchivo) {
        String mime = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
        String extension = extraerExtension(nombreArchivo);

        if (mime.equals("application/pdf") || extension.equals("pdf")) {
            return "PDF";
        }
        if (mime.startsWith("image/") || EXTENSIONES_IMAGEN.contains(extension)) {
            return "IMAGEN";
        }

        throw new ReglaDeNegocioException(
                "Formato de archivo no permitido. Solo se permiten archivos PDF o imágenes (PNG, JPG, JPEG, WEBP, GIF)."
        );
    }

    private String extraerExtension(String nombreArchivo) {
        if (nombreArchivo == null) {
            return "";
        }
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto < 0 || ultimoPunto == nombreArchivo.length() - 1) {
            return "";
        }
        return nombreArchivo.substring(ultimoPunto + 1).toLowerCase(Locale.ROOT);
    }
}