package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udea.integrador.ingenote.application.port.in.ListarAsignaturasUseCase;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {
    private final ListarAsignaturasUseCase asignaturas;

    public AsignaturaController(ListarAsignaturasUseCase asignaturas) {
        this.asignaturas = asignaturas;
    }

    @GetMapping
    public List<AsignaturaResponse> listar() {
        return asignaturas.listar().stream()
                .map(a -> new AsignaturaResponse(a.id(), a.nombre(), a.semestre()))
                .toList();
    }

    public record AsignaturaResponse(UUID id, String nombre, Integer semestre) { }
}
