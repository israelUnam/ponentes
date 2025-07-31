package  mx.unam.sa.ponentes.entity.cuestionarios;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatContenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContenido;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idPregunta")
    private CatPregunta pregunta;

    @Size(min = 2, max = 200)
    @NotEmpty(message = "El nombre de la descripción no puede estar en blanco.")
    private String descripcion;

    boolean otro = false;

    int status = 1;

    Date fecReg = new Date();

    int orden = 0;


    public CatContenido(Long idContenido, CatPregunta pregunta, String descripcion, boolean otro, int orden) {
        this.idContenido = idContenido;
        this.pregunta = pregunta;
        this.descripcion = descripcion;
        this.otro = otro;
        this.orden = orden;
    }
}
