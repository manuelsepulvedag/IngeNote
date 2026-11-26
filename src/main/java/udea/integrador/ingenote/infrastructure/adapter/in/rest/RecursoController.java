package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import udea.integrador.ingenote.application.exception.AlmacenamientoException;
import udea.integrador.ingenote.application.port.in.EditarRecursoUseCase;
import udea.integrador.ingenote.application.port.in.EliminarRecursoUseCase;
import udea.integrador.ingenote.application.port.in.ListarRecursosUseCase;
import udea.integrador.ingenote.application.port.in.SubirRecursoCommand;
import udea.integrador.ingenote.application.port.in.SubirRecursoUseCase;
import udea.integrador.ingenote.application.port.in.ValoracionUseCase;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.ResumenValoracion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {
    private final SubirRecursoUseCase subirRecurso;
    private final EditarRecursoUseCase editarRecurso;
    private final ListarRecursosUseCase listarRecursos;
    private final ValoracionUseCase valoraciones;
    private final EliminarRecursoUseCase eliminarRecurso;

    public RecursoController(SubirRecursoUseCase subirRecurso, EditarRecursoUseCase editarRecurso,
                             ListarRecursosUseCase listarRecursos, ValoracionUseCase valoraciones,
                             EliminarRecursoUseCase eliminarRecurso) {
        this.subirRecurso = subirRecurso;
        this.editarRecurso = editarRecurso;
        this.listarRecursos = listarRecursos;
        this.valoraciones = valoraciones;
        this.eliminarRecurso = eliminarRecurso;
    }

    /** Crea un recurso con un archivo (PDF o imagen) o con un enlace externo. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecursoResponse> subir(
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam(value = "enlace", required = false) String enlace,
            @RequestParam("idAsignatura") UUID idAsignatura,
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            Authentication authentication
    ) {
        Recurso recurso = subirRecurso.subir(comando(authentication, idAsignatura, titulo, descripcion, archivo, enlace));
        return ResponseEntity.status(HttpStatus.CREATED).body(RecursoResponse.desde(recurso));
    }

    /** Edita título, asignatura y descripción; si llega archivo o enlace, reemplaza el adjunto. */
    @PutMapping(value = "/{recursoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RecursoResponse editar(
            @PathVariable UUID recursoId,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam(value = "enlace", required = false) String enlace,
            @RequestParam("idAsignatura") UUID idAsignatura,
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            Authentication authentication
    ) {
        Recurso recurso = editarRecurso.editar(recursoId,
                comando(authentication, idAsignatura, titulo, descripcion, archivo, enlace));
        return RecursoResponse.desde(recurso, valoraciones.resumen(authentication.getName(), recurso.id()));
    }

    @DeleteMapping("/{recursoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID recursoId, Authentication authentication) {
        eliminarRecurso.eliminar(authentication.getName(), recursoId);
    }

    @GetMapping
    public List<RecursoResponse> listar(@RequestParam(value = "idAsignatura", required = false) UUID idAsignatura, Authentication authentication) {
        return listarRecursos.listar(idAsignatura)
                .stream()
                .map(r -> RecursoResponse.desde(r, valoraciones.resumen(authentication.getName(), r.id())))
                .toList();
    }

    private static SubirRecursoCommand comando(Authentication authentication, UUID idAsignatura, String titulo,
                                               String descripcion, MultipartFile archivo, String enlace) {
        byte[] bytes = null;
        String nombre = null;
        String tipo = null;
        if (archivo != null && !archivo.isEmpty()) {
            try {
                bytes = archivo.getBytes();
            } catch (IOException ex) {
                throw new AlmacenamientoException("No fue posible leer el archivo enviado", ex);
            }
            nombre = archivo.getOriginalFilename();
            tipo = archivo.getContentType();
        }
        return new SubirRecursoCommand(authentication.getName(), idAsignatura, titulo, nombre, tipo, bytes,
                descripcion, enlace);
    }

    public record RecursoResponse(
            UUID id,
            String titulo,
            String descripcion,
            String urlStorage,
            String tipoFormato,
            UUID idAsignatura,
            String asignatura,
            UUID idAutor,
            String autor,
            UUID idCarreraAutor,
            LocalDateTime fechaSubida,
            double promedioValoracion,
            long cantidadValoraciones,
            Short valoracionUsuarioActual
    ) {
        public static RecursoResponse desde(Recurso r) {
            return desde(r, ResumenValoracion.vacio(r.id()));
        }
        public static RecursoResponse desde(Recurso r, ResumenValoracion valoracion) {
            return new RecursoResponse(
                    r.id(),
                    r.titulo(),
                    r.descripcion(),
                    r.urlStorage(),
                    r.tipoFormato().nombre(),
                    r.asignatura().id(),
                    r.asignatura().nombre(),
                    r.usuario().id(),
                    r.usuario().nombre(),
                    r.usuario().idCarrera(),
                    r.fechaSubida(),
                    valoracion.promedio(),
                    valoracion.cantidadValoraciones(),
                    valoracion.valoracionUsuarioActual()
            );
        }
    }
}
