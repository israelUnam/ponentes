package mx.unam.sa.ponentes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@NoArgsConstructor
public class Authority implements GrantedAuthority {


    @Id
    @JdbcTypeCode(java.sql.Types.INTEGER)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String authority;

    public Authority(String authority) {
        this.authority = authority;
    }


}
