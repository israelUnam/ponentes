package mx.unam.sa.ponentes.dto;

import lombok.Data;

@Data
public class ComentariosDTO {

    Integer idComentario;
    Long idRespCuestionario;
    String comentario;
    String usuario;
    Long idTema;
    Integer idUser;
    String fechaAct;
}