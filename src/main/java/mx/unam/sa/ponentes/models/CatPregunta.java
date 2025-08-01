package  mx.unam.sa.ponentes.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CatPregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPregunta;

    @Size(min = 3, max = 250)
    @NotEmpty(message = "El nombre de la descripción no puede estar en blanco.")
    private String descripcion;

    @Column(length = 500)
    @Size(max = 500)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "idCatalogo")
    private CatTipoPregunta catPregunta;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idTema")
    private Tema tema;

    @OneToMany(mappedBy = "pregunta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CatContenido> catContenido = new ArrayList<>();

    boolean obligatoria = false;
    int orden = 0;

    Date fecReg = new Date();

    int status = 1;

    public CatPregunta(Integer idPregunta, String descripcion, String observaciones, CatTipoPregunta catPregunta, Tema tema,
            boolean obligatoria, int orden) {
        this.idPregunta = idPregunta;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.catPregunta = catPregunta;
        this.tema = tema;
        this.obligatoria = obligatoria;
        this.orden = orden;
    }
}
