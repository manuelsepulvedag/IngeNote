package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.AccesoDenegadoException;
import udea.integrador.ingenote.application.exception.RecursoNoEncontradoException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.ValoracionUseCase;
import udea.integrador.ingenote.domain.model.ResumenValoracion;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.model.Valoracion;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import udea.integrador.ingenote.domain.port.out.ValoracionPersistencePort;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ValoracionService implements ValoracionUseCase {
    private final ValoracionPersistencePort valoraciones;
    private final UsuarioPersistencePort usuarios;

    public ValoracionService(ValoracionPersistencePort valoraciones, UsuarioPersistencePort usuarios) {
        this.valoraciones = valoraciones;
        this.usuarios = usuarios;
    }

    @Override
    public ResumenValoracion valorar(String correo, UUID recursoId, short estrellas) {
        if (!Valoracion.esValida(estrellas)) {
            throw new ReglaDeNegocioException("La valoración debe estar entre "
                    + Valoracion.MIN_ESTRELLAS + " y " + Valoracion.MAX_ESTRELLAS + " estrellas");
        }
        UUID autor = autorDe(recursoId);
        Usuario usuario = usuario(correo);
        if (autor.equals(usuario.id())) {
            throw new ReglaDeNegocioException("No puede valorar sus propios recursos");
        }
        Optional<Valoracion> previa = valoraciones.buscarPorRecursoYUsuario(recursoId, usuario.id());
        valoraciones.guardar(new Valoracion(
                previa.map(Valoracion::id).orElse(null),
                recursoId,
                usuario.id(),
                estrellas,
                previa.map(Valoracion::fechaCreacion).orElse(LocalDateTime.now())));
        return resumenPara(recursoId, usuario);
    }

    @Override
    public ResumenValoracion retirar(String correo, UUID recursoId) {
        autorDe(recursoId);
        Usuario usuario = usuario(correo);
        valoraciones.buscarPorRecursoYUsuario(recursoId, usuario.id())
                .ifPresent(v -> valoraciones.eliminar(v.id()));
        return resumenPara(recursoId, usuario);
    }

    @Override
    public ResumenValoracion resumen(String correo, UUID recursoId) {
        autorDe(recursoId);
        return resumenPara(recursoId, usuario(correo));
    }

    private ResumenValoracion resumenPara(UUID recursoId, Usuario usuario) {
        Short actual = valoraciones.buscarPorRecursoYUsuario(recursoId, usuario.id())
                .map(Valoracion::estrellas)
                .orElse(null);
        double promedio = Math.round(valoraciones.promedio(recursoId) * 100.0) / 100.0;
        return new ResumenValoracion(recursoId, promedio, valoraciones.cantidadValoraciones(recursoId), actual);
    }

    private UUID autorDe(UUID recursoId) {
        return valoraciones.buscarAutorRecurso(recursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el recurso"));
    }

    private Usuario usuario(String correo) {
        return usuarios.buscarPorCorreo(correo)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado"));
    }
}
