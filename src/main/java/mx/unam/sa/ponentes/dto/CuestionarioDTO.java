package mx.unam.sa.ponentes.dto;
import java.util.List;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class CuestionarioDTO {
    private Long id;
    private String titulo;
    private String subtitulo;
    private String contenido;
    private String fechaCreacion;
    private String parametros;
    private List<NotificacionDTO> notificacion;
    private Integer totalNotificaciones;
}
