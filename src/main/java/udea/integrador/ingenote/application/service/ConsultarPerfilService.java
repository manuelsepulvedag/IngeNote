package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.port.in.ConsultarPerfilUseCase;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;

public class ConsultarPerfilService implements ConsultarPerfilUseCase {
    private final UsuarioPersistencePort usuarios;

    public ConsultarPerfilService(UsuarioPersistencePort usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public Usuario actual(String correo) {
        return usuarios.buscarPorCorreo(correo)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado"));
    }
}
