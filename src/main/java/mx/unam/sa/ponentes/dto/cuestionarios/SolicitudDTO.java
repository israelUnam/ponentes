package  mx.unam.sa.ponentes.dto.cuestionarios;

public interface SolicitudDTO {

    Long getIdRespuesta();
    
    Long getIdTema();

    String getDescTema();

    String getDesPregunta();;

    Boolean getObligatoria();;

    String getObservaciones();;

    Long getIdContenido();;

    String getDesContenido();;

    Boolean getOtro();;

    String getDesCatalogo();

    Long getIdPregunta();

    String getRequired();

    // Requeridas en caso de error y se tenga que regresar que contesto el usuario
    String getRespuesta();
    //En caso de que se tenga que regresar una respuesta adicional
    String getResp_adicional();
    //En caso de que se tenga que regresar el id del documento
    String getIdDocto();
    //En caso de que se tenga que regresar el nombre del documento
    String getNomDocto();
}