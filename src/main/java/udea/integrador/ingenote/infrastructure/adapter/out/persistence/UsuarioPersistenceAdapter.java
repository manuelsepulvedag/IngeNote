package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import udea.integrador.ingenote.domain.model.Rol;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RolJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.RolSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.UsuarioSpringDataRepository;

import java.util.Optional;

@Repository
public class UsuarioPersistenceAdapter implements UsuarioPersistencePort {
    private final UsuarioSpringDataRepository usuarios;
    private final RolSpringDataRepository roles;
    public UsuarioPersistenceAdapter(UsuarioSpringDataRepository usuarios, RolSpringDataRepository roles) { this.usuarios = usuarios; this.roles = roles; }
    public boolean existePorCorreo(String correo) { return usuarios.existsByCorreo(correo); }
    public Optional<Usuario> buscarPorCorreo(String correo) { return usuarios.findByCorreo(correo).map(UsuarioPersistenceMapper::aDominio); }
    public Optional<Rol> buscarRolPorNombre(String nombre) { return roles.findByNombre(nombre).map(UsuarioPersistenceMapper::aDominio); }
    public Usuario guardar(Usuario usuario) {
        RolJpaEntity rol = roles.findById(usuario.rol().id()).orElseThrow();
        return UsuarioPersistenceMapper.aDominio(usuarios.save(UsuarioPersistenceMapper.aEntidadNuevo(usuario, rol)));
    }
}
