package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Carrera de la facultad. Solo lectura: las carreras se administran con SQL. */
@Entity
@Table(name = "tbl_carrera")
public class CarreraJpaEntity {
    @Id
    @Column(name = "id_carrera")
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    /** Una asignatura (p. ej. Cálculo) puede pertenecer a varias carreras. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_carrera_asignatura",
            joinColumns = @JoinColumn(name = "id_carrera"),
            inverseJoinColumns = @JoinColumn(name = "id_asignatura"))
    private Set<AsignaturaJpaEntity> asignaturas = new HashSet<>();

    protected CarreraJpaEntity() { }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public Set<AsignaturaJpaEntity> getAsignaturas() { return asignaturas; }
}
