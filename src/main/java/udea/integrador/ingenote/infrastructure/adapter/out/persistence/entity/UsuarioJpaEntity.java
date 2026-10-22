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
@Table(name = "tbl_usuario")
public class UsuarioJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // UUID producido por el DEFAULT/trigger de PostgreSQL
    @Column(name = "id_usuario")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private RolJpaEntity rol;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "correo", nullable = false)
    private String correo;
    @Column(name = "contrasena_hash", nullable = false)
    private String contrasenaHash;
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    protected UsuarioJpaEntity() { }
    public UsuarioJpaEntity(RolJpaEntity rol, String nombre, String correo, String contrasenaHash, LocalDateTime fechaRegistro) {
        this.rol = rol; this.nombre = nombre; this.correo = correo; this.contrasenaHash = contrasenaHash; this.fechaRegistro = fechaRegistro;
    }
    public UUID getId() { return id; }
    public RolJpaEntity getRol() { return rol; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContrasenaHash() { return contrasenaHash; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
