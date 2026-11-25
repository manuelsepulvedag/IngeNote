package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Recurso;

public interface SubirRecursoUseCase {
    Recurso subir(SubirRecursoCommand command);
}