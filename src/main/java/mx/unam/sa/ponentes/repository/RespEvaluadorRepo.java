package mx.unam.sa.ponentes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.dto.RelacionintDTO;
import mx.unam.sa.ponentes.models.Resp_revision;


@Repository
public interface RespEvaluadorRepo extends JpaRepository<Resp_revision, Integer> {
    List<Resp_revision> findByRespCuestionarioIdRespCuestionarioOrderByFecReg(Long idRespCuestionario);

    List<Resp_revision> findByRespCuestionarioIdRespCuestionarioAndStatusInOrderByFecReg(Long idRespCuestionario, List<Integer> statuses);

    @Query(value = "SELECT idresp_evaluador, id_docto from resp_eva_doctos where idresp_evaluador = ?1", nativeQuery = true)
    List<RelacionintDTO> getRelacionDoctos(int idRespEvaluador);
}
