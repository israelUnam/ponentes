package  mx.unam.sa.ponentes.dto.cuestionarios;

import lombok.Getter;
import lombok.Setter;

@lombok.ToString
@Getter
@Setter
public class SolicitudDTOImp implements SolicitudDTO {

    private Long idRespuesta;
    private Long idTema;
    private String descTema;
    private String desPregunta;
    private Boolean obligatoria;
    private String observaciones;
    private Long idContenido;
    private String desContenido;
    private Boolean otro;
    private String desCatalogo;
    private Long idPregunta;
    private String required;
    private String respuesta;
    private String resp_adicional;
    private String idDocto;
    private String nomDocto;


}
