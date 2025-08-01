package mx.unam.sa.ponentes.dto;

import lombok.Data;

@Data
public class DatoCuestionario {
    int id_eval_cuestionario;
    Long idCuestionario;
    Long idUser;
    String titulo;
    String subtitulo;
    boolean acceso;
    String param;
}
