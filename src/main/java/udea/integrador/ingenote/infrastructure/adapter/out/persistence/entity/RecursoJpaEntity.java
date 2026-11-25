package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_recurso")
public class RecursoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asignatura", nullable = false)
    private AsignaturaJpaEntity asignatura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioJpaEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_formato", nullable = false)
    private TipoFormatoJpaEntity tipoFormato;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "url_storage", nullable = false)
    private String urlStorage;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    protected RecursoJpaEntity() { }

    public RecursoJpaEntity(
            AsignaturaJpaEntity asignatura,
            UsuarioJpaEntity usuario,
            TipoFormatoJpaEntity tipoFormato,
            String titulo,
            String urlStorage,
            LocalDateTime fechaSubida
    ) {
        this.asignatura = asignatura;
        this.usuario = usuario;
        this.tipoFormato = tipoFormato;
        this.titulo = titulo;
        this.urlStorage = urlStorage;
        this.fechaSubida = fechaSubida;
    }

    public UUID getId() { return id; }
    public AsignaturaJpaEntity getAsignatura() { return asignatura; }
    public UsuarioJpaEntity getUsuario() { return usuario; }
    public TipoFormatoJpaEntity getTipoFormato() { return tipoFormato; }
    public String getTitulo() { return titulo; }
    public String getUrlStorage() { return urlStorage; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
}