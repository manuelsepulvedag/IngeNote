package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "tbl_foro_hilo")
public class ForoHiloJpaEntity {
 @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name="id_foro_hilo") private UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_asignatura", nullable=false) private AsignaturaJpaEntity asignatura;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_usuario", nullable=false) private UsuarioJpaEntity usuario;
 @Column(nullable=false) private String titulo;
 @Column(nullable=false, columnDefinition="text") private String contenido;
 @Column(name="fecha_creacion", nullable=false) private LocalDateTime fechaCreacion;
 /** Respuesta marcada como solución (FK a tbl_foro_respuesta con ON DELETE SET NULL). */
 @Column(name="id_respuesta_solucion") private UUID idRespuestaSolucion;
 protected ForoHiloJpaEntity() {}
 public ForoHiloJpaEntity(AsignaturaJpaEntity a, UsuarioJpaEntity u, String t, String c, LocalDateTime f) { asignatura=a;usuario=u;titulo=t;contenido=c;fechaCreacion=f; }
 public UUID getId(){return id;} public AsignaturaJpaEntity getAsignatura(){return asignatura;} public UsuarioJpaEntity getUsuario(){return usuario;} public String getTitulo(){return titulo;} public String getContenido(){return contenido;} public LocalDateTime getFechaCreacion(){return fechaCreacion;}
 public UUID getIdRespuestaSolucion(){return idRespuestaSolucion;}
 public void actualizar(String t,String c){titulo=t;contenido=c;}
 public void marcarSolucion(UUID respuestaId){idRespuestaSolucion=respuestaId;}
}
