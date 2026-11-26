package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import udea.integrador.ingenote.domain.model.Valoracion;
import udea.integrador.ingenote.domain.port.out.ValoracionPersistencePort;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.ValoracionJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.RecursoSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.UsuarioSpringDataRepository;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.repository.ValoracionSpringDataRepository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ValoracionPersistenceAdapter implements ValoracionPersistencePort {
    private final ValoracionSpringDataRepository valoraciones;
    private final RecursoSpringDataRepository recursos;
    private final UsuarioSpringDataRepository usuarios;

    public ValoracionPersistenceAdapter(ValoracionSpringDataRepository valoraciones,
                                        RecursoSpringDataRepository recursos,
                                        UsuarioSpringDataRepository usuarios) {
        this.valoraciones = valoraciones;
        this.recursos = recursos;
        this.usuarios = usuarios;
    }

    private Valoracion aDominio(ValoracionJpaEntity e) {
        return new Valoracion(e.getId(), e.getRecurso().getId(), e.getUsuario().getId(),
                e.getVoto(), e.getFechaCreacion());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UUID> buscarAutorRecurso(UUID recursoId) {
        return recursos.findById(recursoId).map(r -> r.getUsuario().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Valoracion> buscarPorRecursoYUsuario(UUID recursoId, UUID usuarioId) {
        return valoraciones.findByRecursoIdAndUsuarioId(recursoId, usuarioId).map(this::aDominio);
    }

    @Override
    @Transactional
    public Valoracion guardar(Valoracion v) {
        ValoracionJpaEntity e;
        if (v.id() == null) {
            e = new ValoracionJpaEntity(recursos.getReferenceById(v.recursoId()),
                    usuarios.getReferenceById(v.usuarioId()), v.estrellas(), v.fechaCreacion());
        } else {
            e = valoraciones.findById(v.id()).orElseThrow();
            e.cambiarVoto(v.estrellas());
        }
        return aDominio(valoraciones.save(e));
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        valoraciones.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public double promedio(UUID recursoId) {
        Double promedio = valoraciones.promedio(recursoId);
        return promedio == null ? 0 : promedio;
    }

    @Override
    @Transactional(readOnly = true)
    public long cantidadValoraciones(UUID recursoId) {
        return valoraciones.countByRecursoId(recursoId);
    }
}
