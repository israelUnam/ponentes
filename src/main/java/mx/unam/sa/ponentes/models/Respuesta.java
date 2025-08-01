package mx.unam.sa.ponentes.models;

import java.util.Date;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idContenido")
    private CatContenido contenido;

    @Column(length = 300)
    String respuesta;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idDocto")
    Documento documento;

    Date fecReg = new Date();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idRespCuestionario")
    private RespCuestionario respCuestionario;

    @Column(length = 500)
    @Size(min = 3, max = 500)
    private String resp_adicional;

    public String toString() {
        return "Respuesta{" + "idRespuesta=" + idRespuesta.toString() + ", respuesta="
                + respuesta + ", created_at=" + fecReg + ", resp_adicional=" + resp_adicional + '}';
    }
}
