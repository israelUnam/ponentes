package mx.unam.sa.ponentes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.sa.ponentes.models.Documento;



@Repository
public interface DocumentoRepo extends JpaRepository<Documento, Integer> {
    //@Query("SELECT new mx.unam.sa.ponentes.dto.DocumentoDTO(d.idDocto, d.nombre, d.tipo, d.user.id, d.user.username) FROM Documento d WHERE d.idDocto in (?1)")
    //List<DocumentoDTO> getDatosDocto(Integer[] idDocto);

    @Modifying
    @Query(value = "insert INTO respcuest_doctos_rub (id_resp_cuestionario, id_docto) values (?1,?2)", nativeQuery = true)
    void updateRespcuestDoctosRub(Long idRespCuestionario, int idDocto);

    @Query(value = "Select id_docto from respcuest_doctos_rub where id_resp_cuestionario = ?1", nativeQuery = true)
    List<Integer> getRelacionDoctosRub(Long idRespCuestionario);

    @Modifying
    @Query(value = "delete from respcuest_doctos_rub where id_docto = ?1", nativeQuery = true)
    void deleteRelacionDoctosRub(Integer idDocto);
}
