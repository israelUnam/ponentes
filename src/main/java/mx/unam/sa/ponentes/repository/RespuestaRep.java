package mx.unam.sa.ponentes.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.models.Respuesta;


public interface RespuestaRep extends JpaRepository<Respuesta, Long> {
    Respuesta findByContenidoIdContenidoAndRespCuestionarioIdRespCuestionario(Long idContenido,
            Long idRespCuestionario);

    List<Respuesta> findByRespCuestionarioIdRespCuestionario(Long idRespCuestionario);

    @Transactional
    int removeByRespCuestionarioIdRespCuestionario(Long idRespCuestionario);

    Respuesta findByIdRespuestaAndRespCuestionarioIdRespCuestionarioAndContenidoIdContenido(Long idRespuesta, Long idRespCuestionario, Long idContenido);
}