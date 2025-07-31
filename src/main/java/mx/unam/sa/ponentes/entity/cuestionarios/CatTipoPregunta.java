package  mx.unam.sa.ponentes.entity.cuestionarios;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatTipoPregunta {
    @Id
    private Integer idCatalogo;

    @Column(length = 100)
    private String descripcion;

    @Column(length = 25)
    private String observacion;

    @OneToMany(mappedBy = "catPregunta")
    private List<CatPregunta> preguntas;

    public CatTipoPregunta(Integer idCatalogo, String descripcion, String observacion) {
        this.idCatalogo = idCatalogo;
        this.descripcion = descripcion;
        this.observacion = observacion;
    }
}
