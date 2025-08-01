package mx.unam.sa.ponentes.dto;
import lombok.Data;

@Data
public class CuestDetalleDTO {
    Long idRespCuestionario;
    String usuario;
    String nombreProyecto;
    String fechaCaptura;
    String fechaRevision;
    String param;
    String folio;
    //Determina si el evaluador tiene acceso, falso cuando el cuestonario es suyo
    boolean acceso;
}
