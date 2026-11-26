package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.port.in.ListarCarrerasUseCase;
import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.Carrera;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import java.util.Comparator;
import java.util.List;

public class ListarCarrerasService implements ListarCarrerasUseCase {
    private static final Comparator<Asignatura> POR_SEMESTRE =
            Comparator.comparing(Asignatura::semestre, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(Asignatura::nombre, String.CASE_INSENSITIVE_ORDER);

    private final RecursoPersistencePort recursos;

    public ListarCarrerasService(RecursoPersistencePort recursos) {
        this.recursos = recursos;
    }

    @Override
    public List<Carrera> listar() {
        return recursos.listarCarreras().stream()
                .map(c -> new Carrera(c.id(), c.nombre(), c.asignaturas().stream().sorted(POR_SEMESTRE).toList()))
                .sorted(Comparator.comparing(Carrera::nombre, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
