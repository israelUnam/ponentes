package mx.unam.sa.ponentes.models;


import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


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
    private LocalDateTime fecRegustro = LocalDateTime.now();

}