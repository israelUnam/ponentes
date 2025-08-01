package mx.unam.sa.ponentes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.models.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long>{
    
}
