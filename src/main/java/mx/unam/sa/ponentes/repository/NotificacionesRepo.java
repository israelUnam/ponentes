package mx.unam.sa.ponentes.repository;

import java.util.List;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mx.unam.sa.ponentes.models.Notificacion;

@Repository
public interface NotificacionesRepo extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByIdCuestionarioAndIdUserAndRevisadaAndStatusAndFecRegBetween(Long idCuestionario,
            Integer idUsuario, boolean revisada, int status, Date fecReg1, Date fecReg2);
    List<Notificacion> findByIdUserAndIdCuestionario(Integer idUser, Long idRespCuestionario);

    Integer countByIdUserAndIdCuestionario(Integer idUser, Long idRespCuestionario);
}
