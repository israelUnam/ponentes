package mx.unam.sa.ponentes.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;


@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @JdbcTypeCode(java.sql.Types.INTEGER)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany
    private Set<Authority> authorities;

    private String password;
    private String name;
    private String picture;

    @Column(unique = true)
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private String provider;
    private String providerId;
    private boolean enabled;
}