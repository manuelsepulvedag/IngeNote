package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity @Table(name="tbl_valoracion", uniqueConstraints=@UniqueConstraint(columnNames={"id_recurso","id_usuario"}))
public class ValoracionJpaEntity {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="id_valoracion") private UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_recurso",nullable=false) private RecursoJpaEntity recurso;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_usuario",nullable=false) private UsuarioJpaEntity usuario;
 @Column(nullable=false) private short voto; @Column(name="fecha_creacion",nullable=false) private LocalDateTime fechaCreacion;
 protected ValoracionJpaEntity(){} public ValoracionJpaEntity(RecursoJpaEntity r,UsuarioJpaEntity u,short v,LocalDateTime f){recurso=r;usuario=u;voto=v;fechaCreacion=f;}
 public UUID getId(){return id;} public RecursoJpaEntity getRecurso(){return recurso;} public UsuarioJpaEntity getUsuario(){return usuario;} public short getVoto(){return voto;} public LocalDateTime getFechaCreacion(){return fechaCreacion;} public void cambiarVoto(short v){voto=v;}
}
