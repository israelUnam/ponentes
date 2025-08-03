package mx.unam.sa.ponentes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.unam.sa.ponentes.dto.SolicitudDTO;
import mx.unam.sa.ponentes.dto.SolicitudDTOImp;
import mx.unam.sa.ponentes.models.CatContenido;
import mx.unam.sa.ponentes.repository.CatContenidoRepository;


@Service
public class CatContenidoService {
    @Autowired
    CatContenidoRepository contenidoRepository;

    public List<SolicitudDTOImp> findCuestionario(Long idCuestionario) {
        List<SolicitudDTO> solicitudDTOs = contenidoRepository.findCuestionario(idCuestionario);

        List<SolicitudDTOImp> solicitudDTOImps = new ArrayList<>();

        for (SolicitudDTO solicitudDTO : solicitudDTOs) {
            SolicitudDTOImp solicitudDTOImp = new SolicitudDTOImp();
            solicitudDTOImp.setIdRespuesta(solicitudDTO.getIdRespuesta());
            solicitudDTOImp.setIdTema(solicitudDTO.getIdTema());
            solicitudDTOImp.setDescTema(solicitudDTO.getDescTema());
            solicitudDTOImp.setDesPregunta(solicitudDTO.getDesPregunta());
            solicitudDTOImp.setObligatoria(solicitudDTO.getObligatoria());
            solicitudDTOImp.setObservaciones(solicitudDTO.getObservaciones());
            solicitudDTOImp.setIdContenido(solicitudDTO.getIdContenido());
            solicitudDTOImp.setDesContenido(solicitudDTO.getDesContenido());
            solicitudDTOImp.setOtro(solicitudDTO.getOtro());
            solicitudDTOImp.setIdPregunta(solicitudDTO.getIdPregunta());
            solicitudDTOImp.setDesCatalogo(solicitudDTO.getDesCatalogo());
            solicitudDTOImp.setRequired(solicitudDTO.getRequired());
            if (solicitudDTO.getRespuesta().equals(" ")) {
                solicitudDTOImp.setRespuesta("");
            }else{
                solicitudDTOImp.setRespuesta(solicitudDTO.getRespuesta());
            }
            solicitudDTOImp.setResp_adicional(solicitudDTO.getResp_adicional());
            solicitudDTOImp.setIdDocto(solicitudDTO.getIdDocto());
            solicitudDTOImp.setNomDocto(solicitudDTO.getNomDocto());

            solicitudDTOImps.add(solicitudDTOImp);

        }
        

        solicitudDTOImps.replaceAll(solicitudDTOImp -> {
            return solicitudDTOImp;
        });

        return solicitudDTOImps;
    }

    public CatContenido findById(Long id) {
        return contenidoRepository.findById(id).get();
    }

    /**
     * Extreae para las listas multiples los checks que son obligatorios 
     * del tipo {checks_Otro=16_26, checks_Required=chk_16, checks_Section=Adscripción}
     * @param solicitudDTOImps
     * @return
     */
    public Map<String, String> getChkCondicion(List<SolicitudDTOImp> solicitudDTOImps) {
        Map<String, String> chkCondicion = new HashMap<>();
        StringBuilder checksRequired = new StringBuilder();

        for (SolicitudDTOImp solicitudDTOImp : solicitudDTOImps) {
            if (solicitudDTOImp.getDesCatalogo().equals("Multiple")) {
                if (solicitudDTOImp.getObligatoria()) {
                    String key = "chk_" + solicitudDTOImp.getIdPregunta();
                    String checksRequiredString = checksRequired.toString(); // Convert StringBuilder to String

                    if (!checksRequiredString.contains(key + ",")) { // la coma evita que se encuentre un key que
                                                                     // contenga a
                                                                     // otro
                        checksRequired.append(key).append(",");
                    }
                }
            }
        }
        if (checksRequired.length() > 0) { // Check if the StringBuilder is not empty
            checksRequired.deleteCharAt(checksRequired.length() - 1);
        }
        chkCondicion.put("checks_Required", checksRequired.toString());

        return chkCondicion;
    }
}
