package mx.unam.sa.ponentes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.models.CatPregunta;


@Repository
public interface CatPreguntaRepository extends JpaRepository<CatPregunta, Long> {

}
