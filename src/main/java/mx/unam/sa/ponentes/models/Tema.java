package  mx.unam.sa.ponentes.models;

import java.util.ArrayList;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTema;

    @Column(length = 200)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCuestionario")
    private Cuestionario cuestionario;

    @OneToMany(mappedBy = "tema", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CatPregunta> catPreguntas = new ArrayList<>();

    int orden = 0;


    public Tema(Long idTema, String descripcion, Cuestionario cuestionario, int orden) {
        this.idTema = idTema;
        this.descripcion = descripcion;
        this.cuestionario = cuestionario;
        this.orden = orden;
    }
}
