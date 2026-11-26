package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import udea.integrador.ingenote.application.port.in.ValoracionUseCase;
import udea.integrador.ingenote.domain.model.ResumenValoracion;
import java.util.UUID;

@RestController
@RequestMapping("/api/recursos/{recursoId}/valoraciones")
public class ValoracionController {
    private final ValoracionUseCase valoraciones;

    public ValoracionController(ValoracionUseCase valoraciones) {
        this.valoraciones = valoraciones;
    }

    record ValoracionRequest(@NotNull @Min(1) @Max(5) Short estrellas) { }

    /** Crea o reemplaza la valoración del usuario autenticado. Cuerpo: {"estrellas": 1..5}. */
    @PutMapping
    public ResumenValoracion valorar(@PathVariable UUID recursoId,
                                     @Valid @RequestBody ValoracionRequest request,
                                     Authentication authentication) {
        return valoraciones.valorar(authentication.getName(), recursoId, request.estrellas());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void retirar(@PathVariable UUID recursoId, Authentication authentication) {
        valoraciones.retirar(authentication.getName(), recursoId);
    }

    @GetMapping
    public ResumenValoracion resumen(@PathVariable UUID recursoId, Authentication authentication) {
        return valoraciones.resumen(authentication.getName(), recursoId);
    }
}
