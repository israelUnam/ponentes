package mx.unam.sa.ponentes.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RespCuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespCuestionario;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idCuestionario")
    private Cuestionario cuestionario;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idUser")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "respCuestionario", cascade = CascadeType.ALL)
    private List<Respuesta> respuesta;

    Date fecReg = new Date();

    Date cambioCaptura;
    Date fechaRevisado;
    Date fechaTerminado;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Respcuest_doctosRub", joinColumns = @JoinColumn(name = "idRespCuestionario"), inverseJoinColumns = @JoinColumn(name = "idDocto"))
    private Set<Documento> doctos = new HashSet<>();

    int status = 1;

    @Column(length = 20)
    String folio;

    public String toString() {
        return "RespCuestionario{" + "idRespCuestionario=" + idRespCuestionario.toString() + ", user=" + user
                + ", respuesta=" + respuesta + ", created_at=" + fecReg + ", cambioCaptura=" + cambioCaptura
                + ", fechaRevisado=" + fechaRevisado + ", fechaTerminado=" + fechaTerminado + ", status=" + status
                + '}';
    }
}
