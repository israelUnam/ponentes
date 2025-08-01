package mx.unam.sa.ponentes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Folios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idfolio;
    int folio;
    int anio;
    Long idCuestionario;
}