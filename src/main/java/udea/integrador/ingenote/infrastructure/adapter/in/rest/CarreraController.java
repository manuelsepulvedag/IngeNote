package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udea.integrador.ingenote.application.port.in.ListarCarrerasUseCase;
import java.util.List;
import java.util.UUID;

/** Público: el formulario de registro necesita la lista de carreras. */
@RestController
@RequestMapping("/api/carreras")
public class CarreraController {
    private final ListarCarrerasUseCase carreras;

    public CarreraController(ListarCarrerasUseCase carreras) {
        this.carreras = carreras;
    }

    @GetMapping
    public List<CarreraResponse> listar() {
        return carreras.listar().stream()
                .map(c -> new CarreraResponse(c.id(), c.nombre(), c.asignaturas().stream()
                        .map(a -> new AsignaturaController.AsignaturaResponse(a.id(), a.nombre(), a.semestre()))
                        .toList()))
                .toList();
    }

    public record CarreraResponse(UUID id, String nombre, List<AsignaturaController.AsignaturaResponse> asignaturas) { }
}
