package mx.unam.sa.ponentes.repository.cuestionarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mx.unam.sa.ponentes.entity.cuestionarios.CatPregunta;


@Repository
public interface CatPreguntaRepository extends JpaRepository<CatPregunta, Long> {

}
