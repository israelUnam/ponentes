package mx.unam.sa.ponentes.dto;

import lombok.Data;

@Data
public class CuesEvaluaDTO {
    private Long id;
    private String titulo;
    private String subtitulo;
    int capturados;
    int evaluados;
    int dictamenincompleto;
    int terminados;
    String param;
}
