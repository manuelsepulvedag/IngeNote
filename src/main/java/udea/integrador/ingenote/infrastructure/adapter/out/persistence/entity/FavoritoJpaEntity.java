package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_favorito", uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_usuario_favorito"}))
public class FavoritoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_favorito")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioJpaEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_favorito", nullable = false)
    private UsuarioJpaEntity favorito;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    protected FavoritoJpaEntity() { }

    public FavoritoJpaEntity(UsuarioJpaEntity usuario, UsuarioJpaEntity favorito, LocalDateTime fechaCreacion) {
        this.usuario = usuario; this.favorito = favorito; this.fechaCreacion = fechaCreacion;
    }

    public UsuarioJpaEntity getFavorito() { return favorito; }
}
