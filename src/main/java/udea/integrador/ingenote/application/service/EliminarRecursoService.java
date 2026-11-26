package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.exception.RecursoNoEncontradoException;
import udea.integrador.ingenote.application.port.in.EliminarRecursoUseCase;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import java.util.UUID;

public class EliminarRecursoService implements EliminarRecursoUseCase {
    private final RecursoPersistencePort recursos;
    private final UsuarioPersistencePort usuarios;

    public EliminarRecursoService(RecursoPersistencePort recursos, UsuarioPersistencePort usuarios) {
        this.recursos = recursos;
        this.usuarios = usuarios;
    }

    @Override
    public void eliminar(String correo, UUID recursoId) {
        Usuario usuario = usuarios.buscarPorCorreo(correo)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado"));
        Recurso recurso = recursos.buscarPorId(recursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el recurso"));
        boolean esAdmin = "ADMINISTRADOR".equalsIgnoreCase(usuario.rol().nombre());
        if (!esAdmin && !recurso.usuario().id().equals(usuario.id())) {
            throw new AccesoDenegadoException("Solo el autor puede eliminar este recurso");
        }
        recursos.eliminar(recursoId);
    }
}
