package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.port.in.ListarAsignaturasUseCase;
import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import java.util.Comparator;
import java.util.List;

public class ListarAsignaturasService implements ListarAsignaturasUseCase {
    private final RecursoPersistencePort recursos;

    public ListarAsignaturasService(RecursoPersistencePort recursos) {
        this.recursos = recursos;
    }

    @Override
    public List<Asignatura> listar() {
        return recursos.listarAsignaturas().stream()
                .sorted(Comparator.comparing(Asignatura::semestre, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(Asignatura::nombre, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
