package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import udea.integrador.ingenote.domain.model.Rol;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RolJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.CarreraSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.RolSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.UsuarioSpringDataRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioPersistenceAdapter implements UsuarioPersistencePort {
    private final UsuarioSpringDataRepository usuarios;
    private final RolSpringDataRepository roles;
    private final CarreraSpringDataRepository carreras;
    public UsuarioPersistenceAdapter(UsuarioSpringDataRepository usuarios, RolSpringDataRepository roles,
                                     CarreraSpringDataRepository carreras) {
        this.usuarios = usuarios; this.roles = roles; this.carreras = carreras;
    }
    public boolean existePorCorreo(String correo) { return usuarios.existsByCorreo(correo); }
    public Optional<Usuario> buscarPorCorreo(String correo) { return usuarios.findByCorreo(correo).map(UsuarioPersistenceMapper::aDominio); }
    public Optional<Rol> buscarRolPorNombre(String nombre) { return roles.findByNombre(nombre).map(UsuarioPersistenceMapper::aDominio); }
    public Usuario guardar(Usuario usuario) {
        RolJpaEntity rol = roles.findById(usuario.rol().id()).orElseThrow();
        return UsuarioPersistenceMapper.aDominio(usuarios.save(UsuarioPersistenceMapper.aEntidadNuevo(usuario, rol)));
    }
    public boolean existeCarrera(UUID idCarrera) { return carreras.existsById(idCarrera); }
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(UUID id) { return usuarios.findById(id).map(UsuarioPersistenceMapper::aDominio); }
    @Transactional
    public Usuario actualizar(Usuario usuario) {
        UsuarioJpaEntity entidad = usuarios.findById(usuario.id()).orElseThrow();
        entidad.actualizar(usuario.nombre(), usuario.idCarrera(), usuario.contrasenaHash());
        return UsuarioPersistenceMapper.aDominio(usuarios.save(entidad));
    }
    @Transactional
    public void eliminar(UUID id) { usuarios.deleteById(id); }
}
