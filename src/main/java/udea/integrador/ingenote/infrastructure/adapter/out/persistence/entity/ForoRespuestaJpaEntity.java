package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity @Table(name="tbl_foro_respuesta")
public class ForoRespuestaJpaEntity {
 @Id @GeneratedValue(strategy=GenerationType.UUID) @Column(name="id_foro_respuesta") private UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_foro_hilo",nullable=false) private ForoHiloJpaEntity hilo;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_usuario",nullable=false) private UsuarioJpaEntity usuario;
 @Column(nullable=false,columnDefinition="text") private String contenido;
 @Column(name="fecha_creacion",nullable=false) private LocalDateTime fechaCreacion;
 protected ForoRespuestaJpaEntity(){} public ForoRespuestaJpaEntity(ForoHiloJpaEntity h,UsuarioJpaEntity u,String c,LocalDateTime f){hilo=h;usuario=u;contenido=c;fechaCreacion=f;}
 public UUID getId(){return id;} public ForoHiloJpaEntity getHilo(){return hilo;} public UsuarioJpaEntity getUsuario(){return usuario;} public String getContenido(){return contenido;} public LocalDateTime getFechaCreacion(){return fechaCreacion;} public void actualizar(String c){contenido=c;}
}
