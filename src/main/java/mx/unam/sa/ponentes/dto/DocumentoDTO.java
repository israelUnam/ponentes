package mx.unam.sa.ponentes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentoDTO {
    Integer idDocto;
    String nombre;
    String tipo;
    Long idUser;
    String username;
    String param;

    public DocumentoDTO(Integer idDocto, String nombre, String tipo, Long idUser, String username) {
        this.idDocto = idDocto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idUser = idUser;
        this.username = username;
    }
}
