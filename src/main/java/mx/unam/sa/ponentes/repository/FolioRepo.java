package mx.unam.sa.ponentes.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mx.unam.sa.ponentes.models.Folios;


@Repository
public interface FolioRepo extends JpaRepository<Folios, Integer> {
    Optional<Folios> findByAnioAndIdCuestionario(int anio, Long idCuestionario);
}