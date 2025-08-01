package mx.unam.sa.ponentes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.models.Cuestionario;

@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Long> {
    List<Cuestionario> findByStatus(int status);
    List<Cuestionario> findByidCuestionarioIn(List<Long> idCuestionarios);
}
