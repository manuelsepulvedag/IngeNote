package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Usuario;

public interface ConsultarPerfilUseCase {
    /** Usuario dueño del token con el que se hace la petición. */
    Usuario actual(String correo);
}
