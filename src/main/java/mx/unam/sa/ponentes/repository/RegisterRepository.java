package mx.unam.sa.ponentes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mx.unam.sa.ponentes.models.Register;


@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {
}