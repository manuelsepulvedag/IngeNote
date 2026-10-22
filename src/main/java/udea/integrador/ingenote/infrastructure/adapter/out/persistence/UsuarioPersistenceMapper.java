package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import udea.integrador.ingenote.domain.model.Rol;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RolJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;

/** Único lugar donde los modelos puros cruzan la frontera JPA. */
public final class UsuarioPersistenceMapper {
    private UsuarioPersistenceMapper() { }
    public static Rol aDominio(RolJpaEntity entity) { return new Rol(entity.getId(), entity.getNombre()); }
    public static Usuario aDominio(UsuarioJpaEntity entity) {
        return new Usuario(entity.getId(), aDominio(entity.getRol()), entity.getNombre(), entity.getCorreo(), entity.getContrasenaHash(), entity.getFechaRegistro());
    }
    public static UsuarioJpaEntity aEntidadNuevo(Usuario usuario, RolJpaEntity rol) {
        return new UsuarioJpaEntity(rol, usuario.nombre(), usuario.correo(), usuario.contrasenaHash(), usuario.fechaRegistro());
    }
}
