package mx.unam.sa.ponentes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
