package mx.unam.sa.ponentes.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String username;
    private Date fecReg = new Date();
    @Column(length = 200)
    private String ip;
    @Column(columnDefinition = "TEXT")
    private String event;

    public Register(String username, String ip, String event) {
        this.username = username;
        this.ip = ip;
        this.event = event;
    }
}
