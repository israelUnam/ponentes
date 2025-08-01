package mx.unam.sa.ponentes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CuestionariosRespDTO {
    Long idRespCuestionario;
    String usuario;
    String fechaSolicitud;
    String estado;
    String cuestionario;
    String parametros;
    String folio;
}
