package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_rol")
public class RolJpaEntity {
    @Id
    @Column(name = "id_rol")
    private Integer id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    protected RolJpaEntity() { }
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
}
