package mx.unam.sa.ponentes.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    // Eagerly load the user's authorities, such that they are available in the returned User object
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findByUsername(String username);


}
