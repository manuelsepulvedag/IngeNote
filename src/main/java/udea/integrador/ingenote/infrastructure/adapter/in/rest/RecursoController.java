package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import udea.integrador.ingenote.application.exception.AlmacenamientoException;
import udea.integrador.ingenote.application.port.in.ListarRecursosUseCase;
import udea.integrador.ingenote.application.port.in.SubirRecursoCommand;
import udea.integrador.ingenote.application.port.in.SubirRecursoUseCase;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.application.port.in.ValoracionUseCase;
import udea.integrador.ingenote.domain.model.ResumenValoracion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {
    private final SubirRecursoUseCase subirRecurso;
    private final ListarRecursosUseCase listarRecursos;
    private final ValoracionUseCase valoraciones;

    public RecursoController(SubirRecursoUseCase subirRecurso, ListarRecursosUseCase listarRecursos, ValoracionUseCase valoraciones) {
        this.subirRecurso = subirRecurso;
        this.listarRecursos = listarRecursos;
        this.valoraciones = valoraciones;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecursoResponse> subir(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("idAsignatura") UUID idAsignatura,
            @RequestParam("titulo") String titulo,
            Authentication authentication
    ) {
        byte[] bytes;
        try {
            bytes = archivo.getBytes();
        } catch (IOException ex) {
            throw new AlmacenamientoException("No fue posible leer el archivo enviado", ex);
        }

        Recurso recurso = subirRecurso.subir(new SubirRecursoCommand(
                authentication.getName(),
                idAsignatura,
                titulo,
                archivo.getOriginalFilename(),
                archivo.getContentType(),
                bytes
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(RecursoResponse.desde(recurso));
    }

    @GetMapping
    public List<RecursoResponse> listar(@RequestParam(value = "idAsignatura", required = false) UUID idAsignatura, Authentication authentication) {
        return listarRecursos.listar(idAsignatura)
                .stream()
                .map(r -> RecursoResponse.desde(r, valoraciones.resumen(authentication.getName(), r.id())))
                .toList();
    }

    public record RecursoResponse(
            UUID id,
            String titulo,
            String urlStorage,
            String tipoFormato,
            UUID idAsignatura,
            String asignatura,
            String autor,
            LocalDateTime fechaSubida,
            long puntajeNeto,
            long cantidadVotos,
            Short votoUsuarioActual
    ) {
        public static RecursoResponse desde(Recurso r) {
            return desde(r, new ResumenValoracion(r.id(), 0, 0, null));
        }
        public static RecursoResponse desde(Recurso r, ResumenValoracion valoracion) {
            return new RecursoResponse(
                    r.id(),
                    r.titulo(),
                    r.urlStorage(),
                    r.tipoFormato().nombre(),
                    r.asignatura().id(),
                    r.asignatura().nombre(),
                    r.usuario().nombre(),
                    r.fechaSubida(),
                    valoracion.puntajeNeto(),
                    valoracion.cantidadVotos(),
                    valoracion.votoUsuarioActual()
            );
        }
    }
}
