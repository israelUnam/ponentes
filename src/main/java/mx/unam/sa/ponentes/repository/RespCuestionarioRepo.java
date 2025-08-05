package mx.unam.sa.ponentes.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.sa.ponentes.models.RespCuestionario;



@Repository
public interface RespCuestionarioRepo extends JpaRepository<RespCuestionario, Long> {
    List<RespCuestionario> findByUserIdAndCuestionarioIdCuestionario(Integer idUser, Long idCuestionario);

    RespCuestionario findByUserIdAndIdRespCuestionario(Integer idUser, Long idRespCuestionario);

    int countByCuestionarioIdCuestionarioAndStatus(Long idCuestionario, int status);

    List<RespCuestionario> findByCuestionarioIdCuestionarioAndStatus(Long idCuestionario, int status);
    List<RespCuestionario> findByCuestionarioIdCuestionarioAndStatusAndUserIdNot(Long idCuestionario, int status, Long idUser);

    List<RespCuestionario> findByCuestionarioIdCuestionarioAndStatusAndFechaTerminadoBetween(Long idCuestionario, int status, 
        LocalDateTime startDate, LocalDateTime endDate);

    List<RespCuestionario> findByCuestionarioIdCuestionarioAndStatusAndUserIdNotAndFechaTerminadoBetween(Long idCuestionario, int status, Long idUser,
        LocalDateTime startDate, LocalDateTime endDate);        

    @Query(value = "SELECT id_docto FROM respcuest_doctos_rub WHERE id_resp_cuestionario = ?1 ", nativeQuery = true)
    List<Integer> findDoctosRub(Long idRespCuestionario);

    @Query(value = "SELECT DISTINCT YEAR(fec_reg) FROM resp_cuestionario WHERE  id_cuestionario = ?1 ORDER BY YEAR(fec_reg) DESC", nativeQuery = true)
    List<Integer> findDistinctYears(Long idRespCuestionario);
}

