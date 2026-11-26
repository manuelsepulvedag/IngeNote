package udea.integrador.ingenote.infrastructure.adapter.out.persistence;

import udea.integrador.ingenote.domain.model.Asignatura;
import udea.integrador.ingenote.domain.model.Recurso;
import udea.integrador.ingenote.domain.model.TipoFormato;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.AsignaturaJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.RecursoJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.TipoFormatoJpaEntity;
import udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;

public final class RecursoPersistenceMapper {
    private RecursoPersistenceMapper() { }

    public static Asignatura aDominio(AsignaturaJpaEntity entity) {
        return new Asignatura(entity.getId(), entity.getNombre(), entity.getSemestre());
    }

    public static TipoFormato aDominio(TipoFormatoJpaEntity entity) {
        return new TipoFormato(entity.getId(), entity.getNombre());
    }

    public static Recurso aDominio(RecursoJpaEntity entity) {
        return new Recurso(
                entity.getId(),
                UsuarioPersistenceMapper.aDominio(entity.getUsuario()),
                aDominio(entity.getAsignatura()),
                aDominio(entity.getTipoFormato()),
                entity.getTitulo(),
                entity.getUrlStorage(),
                entity.getFechaSubida(),
                entity.getDescripcion()
        );
    }

    public static RecursoJpaEntity aEntidadNuevo(
            Recurso recurso,
            UsuarioJpaEntity usuario,
            AsignaturaJpaEntity asignatura,
            TipoFormatoJpaEntity tipoFormato
    ) {
        return new RecursoJpaEntity(
                asignatura,
                usuario,
                tipoFormato,
                recurso.titulo(),
                recurso.urlStorage(),
                recurso.fechaSubida(),
                recurso.descripcion()
        );
    }
}