package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.port.in.ListarRecursosUseCase;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;

import java.util.List;
import java.util.UUID;

public class ListarRecursosService implements ListarRecursosUseCase {
    private final RecursoPersistencePort recursos;

    public ListarRecursosService(RecursoPersistencePort recursos) {
        this.recursos = recursos;
    }

    @Override
    public List<Recurso> listar(UUID idAsignatura) {
        if (idAsignatura == null) {
            return recursos.listarTodos();
        }
        return recursos.listarPorAsignatura(idAsignatura);
    }
}