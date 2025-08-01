package mx.unam.sa.ponentes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.unam.sa.ponentes.models.Folios;
import mx.unam.sa.ponentes.repository.FolioRepo;



@Service
public class FolioService {
    @Autowired
    private FolioRepo folioRepo;

    public String getFolio(int anio, Long idCuestionario, String claveCuestionario) {
        String salida = "";
        Folios folio = folioRepo.findByAnioAndIdCuestionario(anio, idCuestionario).orElse(null);

        if (folio != null) {
            folio.setFolio(folio.getFolio() + 1);
        } else {
            folio = new Folios();
            folio.setAnio(anio);
            folio.setIdCuestionario(idCuestionario);
            folio.setFolio(1);
        }
        folioRepo.save(folio);
        salida = claveCuestionario + "/" + folio.getFolio() + "/" + anio;
        return salida;
    }
}
