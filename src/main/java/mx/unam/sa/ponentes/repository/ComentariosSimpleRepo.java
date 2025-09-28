package mx.unam.sa.ponentes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.models.ComentariosSimple;


@Repository
public interface ComentariosSimpleRepo extends JpaRepository<ComentariosSimple, Integer> {
    ComentariosSimple findByidUserAndIdRespCuestionarioAndIdTema(Integer idUser, Long idResp, Long idTema);

    List<ComentariosSimple> findByidUserNotAndIdRespCuestionarioAndIdTemaOrderByFecRegAsc(Integer idUser, Long idResp,
            Long idTema);
}
