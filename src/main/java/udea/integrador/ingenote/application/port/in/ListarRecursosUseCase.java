package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Recurso;

import java.util.List;
import java.util.UUID;

public interface ListarRecursosUseCase {
    List<Recurso> listar(UUID idAsignatura);
}