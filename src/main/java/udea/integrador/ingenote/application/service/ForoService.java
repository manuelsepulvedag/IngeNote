package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.exception.RecursoNoEncontradoException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.ForoUseCase;
import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.ForoHilo;
import udea.integrador.ingenote.domain.model.ForoRespuesta;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.ForoPersistencePort;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ForoService implements ForoUseCase {
    private final ForoPersistencePort foros;
    private final RecursoPersistencePort recursos;
    private final UsuarioPersistencePort usuarios;

    public ForoService(ForoPersistencePort foros, RecursoPersistencePort recursos, UsuarioPersistencePort usuarios) {
        this.foros = foros;
        this.recursos = recursos;
        this.usuarios = usuarios;
    }

    @Override
    public ForoHilo crearHilo(String correo, UUID asignaturaId, String titulo, String contenido) {
        Usuario usuario = usuario(correo);
        Asignatura asignatura = recursos.buscarAsignaturaPorId(asignaturaId)
                .orElseThrow(() -> new ReglaDeNegocioException("No existe la asignatura"));
        return foros.guardarHilo(new ForoHilo(null, asignatura, usuario, texto(titulo, "El título"),
                texto(contenido, "El contenido"), LocalDateTime.now(), null));
    }

    @Override
    public ForoHilo editarHilo(String correo, UUID hiloId, String titulo, String contenido) {
        Usuario usuario = usuario(correo);
        ForoHilo hilo = detalle(hiloId);
        exigirAutor(usuario, hilo.autor());
        return foros.guardarHilo(new ForoHilo(hilo.id(), hilo.asignatura(), hilo.autor(), texto(titulo, "El título"),
                texto(contenido, "El contenido"), hilo.fechaCreacion(), hilo.idRespuestaSolucion()));
    }

    @Override
    public void eliminarHilo(String correo, UUID hiloId) {
        exigirAutor(usuario(correo), detalle(hiloId).autor());
        foros.eliminarHilo(hiloId);
    }

    @Override
    public List<ForoHilo> listar(UUID asignaturaId, int pagina, int tamano) {
        return foros.listarHilos(asignaturaId, pagina, tamano);
    }

    @Override
    public long contarHilos(UUID asignaturaId) {
        return foros.contarHilos(asignaturaId);
    }

    @Override
    public long contarRespuestas(UUID hiloId) {
        return foros.contarRespuestas(hiloId);
    }

    @Override
    public ForoHilo detalle(UUID hiloId) {
        return foros.buscarHilo(hiloId).orElseThrow(() -> new RecursoNoEncontradoException("No existe el hilo"));
    }

    @Override
    public ForoRespuesta responder(String correo, UUID hiloId, String contenido) {
        return foros.guardarRespuesta(new ForoRespuesta(null, detalle(hiloId).id(), usuario(correo),
                texto(contenido, "El contenido"), LocalDateTime.now()));
    }

    @Override
    public ForoRespuesta editarRespuesta(String correo, UUID respuestaId, String contenido) {
        Usuario usuario = usuario(correo);
        ForoRespuesta respuesta = respuesta(respuestaId);
        exigirAutor(usuario, respuesta.autor());
        return foros.guardarRespuesta(new ForoRespuesta(respuesta.id(), respuesta.hiloId(), respuesta.autor(),
                texto(contenido, "El contenido"), respuesta.fechaCreacion()));
    }

    @Override
    public void eliminarRespuesta(String correo, UUID respuestaId) {
        exigirAutor(usuario(correo), respuesta(respuestaId).autor());
        foros.eliminarRespuesta(respuestaId);
    }

    @Override
    public List<ForoRespuesta> respuestas(UUID hiloId) {
        detalle(hiloId);
        return foros.listarRespuestas(hiloId);
    }

    /**
     * Solo el autor del hilo marca la solución, y debe ser una respuesta de otra persona.
     * Marcar de nuevo la respuesta que ya es solución la desmarca.
     */
    @Override
    public ForoHilo marcarSolucion(String correo, UUID hiloId, UUID respuestaId) {
        Usuario usuario = usuario(correo);
        ForoHilo hilo = detalle(hiloId);
        if (!usuario.id().equals(hilo.autor().id())) {
            throw new AccesoDenegadoException("Solo quien hizo la pregunta puede elegir la solución");
        }
        ForoRespuesta respuesta = respuesta(respuestaId);
        if (!hiloId.equals(respuesta.hiloId())) {
            throw new ReglaDeNegocioException("La respuesta no pertenece al hilo");
        }
        if (respuesta.autor().id().equals(usuario.id())) {
            throw new ReglaDeNegocioException("No puede marcar su propia respuesta como solución");
        }
        UUID nueva = respuestaId.equals(hilo.idRespuestaSolucion()) ? null : respuestaId;
        return foros.marcarSolucion(hiloId, nueva);
    }

    private Usuario usuario(String correo) {
        return usuarios.buscarPorCorreo(correo)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado"));
    }

    private ForoRespuesta respuesta(UUID respuestaId) {
        return foros.buscarRespuesta(respuestaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la respuesta"));
    }

    private static boolean esAdmin(Usuario usuario) {
        return "ADMINISTRADOR".equalsIgnoreCase(usuario.rol().nombre());
    }

    private static void exigirAutor(Usuario usuario, Usuario autor) {
        if (!esAdmin(usuario) && !usuario.id().equals(autor.id())) {
            throw new AccesoDenegadoException("No tiene permiso sobre este contenido");
        }
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ReglaDeNegocioException(campo + " es obligatorio");
        }
        return valor.trim();
    }
}
