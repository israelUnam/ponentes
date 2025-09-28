package mx.unam.sa.ponentes.models;

import java.util.Date;
import java.util.HashSet;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Resp_revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idresp_evaluador;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idRespCuestionario")
    private RespCuestionario respCuestionario;

    String user;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    Date fecParaRespuesta;
    Date fecReg = new Date();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Resp_rev_doctos", joinColumns = @JoinColumn(name = "idresp_evaluador"), inverseJoinColumns = @JoinColumn(name = "idDocto"))
    private Set<Documento> doctos = new HashSet<>();

    int numRespuesta = 0;

    int status = 1;
}
