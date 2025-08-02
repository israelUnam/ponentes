package mx.unam.sa.ponentes.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotifica;
    @Column(columnDefinition = "TEXT")
    String notificacion;
    String urlDestino;
    Date fecReg = new Date();
    boolean revisada = false;
    int status = 1;
    Integer idUser;
    Long idCuestionario;

    public Notificacion(String notificacion, String urlDestino, Integer idUser, Long idCuestionario) {
        this.notificacion = notificacion;
        this.urlDestino = urlDestino;
        this.idUser = idUser;
        this.idCuestionario = idCuestionario;
    }
}
