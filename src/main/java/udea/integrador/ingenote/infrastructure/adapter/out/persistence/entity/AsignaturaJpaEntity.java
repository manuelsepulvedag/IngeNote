package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tbl_asignatura")
public class AsignaturaJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "semestre", nullable = false)
    private Integer semestre;

    protected AsignaturaJpaEntity() { }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getSemestre() { return semestre; }
}