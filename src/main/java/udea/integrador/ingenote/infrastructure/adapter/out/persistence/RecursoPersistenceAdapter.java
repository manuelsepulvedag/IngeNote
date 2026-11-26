package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.TipoFormato;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.AsignaturaJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RecursoJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.TipoFormatoJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.AsignaturaSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.RecursoSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.TipoFormatoSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.UsuarioSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.ValoracionSpringDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecursoPersistenceAdapter implements RecursoPersistencePort {
    private final RecursoSpringDataRepository recursos;
    private final UsuarioSpringDataRepository usuarios;
    private final AsignaturaSpringDataRepository asignaturas;
    private final TipoFormatoSpringDataRepository tiposFormato;
    private final ValoracionSpringDataRepository valoraciones;

    public RecursoPersistenceAdapter(
            RecursoSpringDataRepository recursos,
            UsuarioSpringDataRepository usuarios,
            AsignaturaSpringDataRepository asignaturas,
            TipoFormatoSpringDataRepository tiposFormato,
            ValoracionSpringDataRepository valoraciones
    ) {
        this.recursos = recursos;
        this.usuarios = usuarios;
        this.asignaturas = asignaturas;
        this.tiposFormato = tiposFormato;
        this.valoraciones = valoraciones;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asignatura> listarAsignaturas() {
        return asignaturas.findAll().stream().map(RecursoPersistenceMapper::aDominio).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recurso> buscarPorId(UUID id) {
        return recursos.findById(id).map(RecursoPersistenceMapper::aDominio);
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        valoraciones.deleteByRecursoId(id);
        recursos.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asignatura> buscarAsignaturaPorId(UUID idAsignatura) {
        return asignaturas.findById(idAsignatura).map(RecursoPersistenceMapper::aDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoFormato> buscarTipoFormatoPorNombre(String nombre) {
        return tiposFormato.findByNombre(nombre).map(RecursoPersistenceMapper::aDominio);
    }

    @Override
    @Transactional
    public Recurso guardar(Recurso recurso) {
        UsuarioJpaEntity usuario = usuarios.findById(recurso.usuario().id()).orElseThrow();
        AsignaturaJpaEntity asignatura = asignaturas.findById(recurso.asignatura().id()).orElseThrow();
        TipoFormatoJpaEntity tipoFormato = tiposFormato.findById(recurso.tipoFormato().id()).orElseThrow();

        RecursoJpaEntity guardado = recursos.save(
                RecursoPersistenceMapper.aEntidadNuevo(recurso, usuario, asignatura, tipoFormato)
        );
        return RecursoPersistenceMapper.aDominio(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recurso> listarTodos() {
        return recursos.findAllByOrderByFechaSubidaDesc()
                .stream()
                .map(RecursoPersistenceMapper::aDominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recurso> listarPorAsignatura(UUID idAsignatura) {
        return recursos.findByAsignaturaIdOrderByFechaSubidaDesc(idAsignatura)
                .stream()
                .map(RecursoPersistenceMapper::aDominio)
                .toList();
    }
}