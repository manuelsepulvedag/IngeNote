package udea.integrador.ingenote.application.port.in;

import udea.integrador.ingenote.domain.model.Usuario;

public interface RegistrarUsuarioUseCase {
    Usuario registrar(RegistroUsuarioCommand command);
}
