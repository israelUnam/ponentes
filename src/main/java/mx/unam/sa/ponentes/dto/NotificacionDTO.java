package mx.unam.sa.ponentes.dto;

import lombok.Data;

@Data
public class NotificacionDTO {
    Long idNotificacion;
    String notificacion;
    String urlDestino;
    String fecReg;
    String param;
}
