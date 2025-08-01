package mx.unam.sa.ponentes.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocto;

    @Lob
    @Column(length = 16777215)
    private byte[] data;

    int size;

    @Column(length = 300)
    String nombre;

    @Column(length = 150)
    String tipo;

    Date fecReg = new Date();

}
