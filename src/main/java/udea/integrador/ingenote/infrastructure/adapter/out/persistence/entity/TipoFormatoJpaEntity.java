package udea.integrador.ingenote.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_tipo_formato")
public class TipoFormatoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_formato")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    protected TipoFormatoJpaEntity() { }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
}