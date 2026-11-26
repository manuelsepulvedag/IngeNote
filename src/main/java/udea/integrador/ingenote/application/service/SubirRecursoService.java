package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.exception.RecursoNoEncontradoException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.EditarRecursoUseCase;
import udea.integrador.ingenote.application.port.in.SubirRecursoCommand;
import udea.integrador.ingenote.application.port.in.SubirRecursoUseCase;
import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.TipoFormato;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.ArchivoStoragePort;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/** Crea y edita recursos: un archivo (PDF o imagen) o un enlace externo. */
public class SubirRecursoService implements SubirRecursoUseCase, EditarRecursoUseCase {
    private static final long TAMANO_MAXIMO_BYTES = 20L * 1024L * 1024L; // 20 MB
    private static final int DESCRIPCION_MAXIMA = 300;
    private static final int ENLACE_MAXIMO = 2000;
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
        validarCampos(command);
        if (!command.traeArchivo() && !command.traeEnlace()) {
            throw new ReglaDeNegocioException("Debe adjuntar un archivo o un enlace");
        }
        Usuario usuario = usuario(command.correoUsuario());
        Asignatura asignatura = asignatura(command.idAsignatura());
        Adjunto adjunto = guardarAdjunto(command, asignatura);

        return recursos.guardar(new Recurso(
                null,
                usuario,
                asignatura,
                adjunto.tipo(),
                command.titulo().trim(),
                adjunto.url(),
                LocalDateTime.now(),
                descripcion(command.descripcion())
        ));
    }

    @Override
    public Recurso editar(UUID recursoId, SubirRecursoCommand command) {
        validarCampos(command);
        Usuario usuario = usuario(command.correoUsuario());
        Recurso actual = recursos.buscarPorId(recursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el recurso"));
        boolean esAdmin = "ADMINISTRADOR".equalsIgnoreCase(usuario.rol().nombre());
        if (!esAdmin && !actual.usuario().id().equals(usuario.id())) {
            throw new AccesoDenegadoException("Solo el autor puede editar este recurso");
        }
        Asignatura asignatura = asignatura(command.idAsignatura());
        Adjunto adjunto = (command.traeArchivo() || command.traeEnlace())
                ? guardarAdjunto(command, asignatura)
                : new Adjunto(actual.tipoFormato(), actual.urlStorage());

        return recursos.actualizar(new Recurso(
                actual.id(),
                actual.usuario(),
                asignatura,
                adjunto.tipo(),
                command.titulo().trim(),
                adjunto.url(),
                actual.fechaSubida(),
                descripcion(command.descripcion())
        ));
    }

    private record Adjunto(TipoFormato tipo, String url) { }

    /** Sube el archivo al almacenamiento o valida el enlace. El archivo tiene prioridad. */
    private Adjunto guardarAdjunto(SubirRecursoCommand command, Asignatura asignatura) {
        if (command.traeArchivo()) {
            if (command.contenido().length > TAMANO_MAXIMO_BYTES) {
                throw new ReglaDeNegocioException("El archivo supera el tamaño máximo permitido de 20 MB");
            }
            TipoFormato tipo = tipoFormato(detectarTipoFormato(command.contentType(), command.nombreArchivo()));
            String contentType = (command.contentType() == null || command.contentType().isBlank())
                    ? "application/octet-stream"
                    : command.contentType();
            String url = storage.subir("asignatura-" + asignatura.id(), command.nombreArchivo(), contentType, command.contenido());
            return new Adjunto(tipo, url);
        }
        return new Adjunto(tipoFormato("URL"), validarEnlace(command.enlace()));
    }

    private void validarCampos(SubirRecursoCommand command) {
        if (command.titulo() == null || command.titulo().isBlank()) {
            throw new ReglaDeNegocioException("El título del recurso es obligatorio");
        }
        if (command.idAsignatura() == null) {
            throw new ReglaDeNegocioException("La asignatura es obligatoria");
        }
        if (command.descripcion() != null && command.descripcion().trim().length() > DESCRIPCION_MAXIMA) {
            throw new ReglaDeNegocioException("La descripción admite máximo " + DESCRIPCION_MAXIMA + " caracteres");
        }
    }

    private static String descripcion(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

    /** Solo se aceptan enlaces http(s) con dominio. */
    private static String validarEnlace(String enlace) {
        String valor = enlace.trim();
        if (valor.length() > ENLACE_MAXIMO) {
            throw new ReglaDeNegocioException("El enlace es demasiado largo");
        }
        try {
            URI uri = new URI(valor);
            String esquema = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
            if (!(esquema.equals("http") || esquema.equals("https")) || uri.getHost() == null) {
                throw new ReglaDeNegocioException("El enlace debe empezar por http:// o https://");
            }
            return valor;
        } catch (java.net.URISyntaxException e) {
            throw new ReglaDeNegocioException("El enlace no es válido");
        }
    }

    private Usuario usuario(String correo) {
        return usuarios.buscarPorCorreo(correo.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ReglaDeNegocioException("No se encontró el usuario autenticado"));
    }

    private Asignatura asignatura(UUID idAsignatura) {
        return recursos.buscarAsignaturaPorId(idAsignatura)
                .orElseThrow(() -> new ReglaDeNegocioException("No existe la asignatura con id " + idAsignatura));
    }

    private TipoFormato tipoFormato(String nombre) {
        return recursos.buscarTipoFormatoPorNombre(nombre)
                .orElseThrow(() -> new ReglaDeNegocioException("No existe el tipo de formato " + nombre + " en la base de datos"));
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
