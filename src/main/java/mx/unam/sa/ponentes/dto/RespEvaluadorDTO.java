package mx.unam.sa.ponentes.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RespEvaluadorDTO {
    private Integer idresp_evaluador;
    private Long idRespCuestionario;
    private String observaciones;
    private String fecParaRespuesta;
    private String fecReg;
    private List<Map<String, String>> documentos;
    private int status;
}
