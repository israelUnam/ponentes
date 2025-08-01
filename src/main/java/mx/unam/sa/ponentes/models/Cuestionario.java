package  mx.unam.sa.ponentes.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cuestionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuestionario;

    @Column(length = 150)
    @Size(min = 3, max = 150)
    @NotEmpty(message = "El titulo del cuestionario no puede estar en blanco.")
    private String titulo;

    @Column(length = 150)
    @Size(min = 3, max = 150)
    private String subtitulo;

    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = "El contenido no puede estar en blanco.")
    private String contenido;

    Date fecReg = new Date();

    int status = 1;

    @Column(length = 10)
    String claveCuestionario;

    @OneToMany(mappedBy = "cuestionario", cascade = CascadeType.ALL)
    private List<Tema> temas = new ArrayList<>();

    public Cuestionario(Long idCuestionario, String titulo, String subtitulo, String contenido, String claveCuestionario) {
        this.idCuestionario = idCuestionario;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.contenido = contenido;
        this.claveCuestionario = claveCuestionario;
    }
}
