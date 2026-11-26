package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.ResumenValoracion;
import java.util.UUID;

public interface ValoracionUseCase {
    /** Crea o reemplaza la valoración (1 a 5 estrellas) del usuario sobre el recurso. */
    ResumenValoracion valorar(String correo, UUID recursoId, short estrellas);
    ResumenValoracion retirar(String correo, UUID recursoId);
    ResumenValoracion resumen(String correo, UUID recursoId);
}
