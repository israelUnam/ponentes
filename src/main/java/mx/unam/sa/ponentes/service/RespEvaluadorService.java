package mx.unam.sa.ponentes.service;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.dto.DocumentoDTO;
import mx.unam.sa.ponentes.dto.RespEvaluadorDTO;
import mx.unam.sa.ponentes.models.Documento;
import mx.unam.sa.ponentes.models.RespCuestionario;
import mx.unam.sa.ponentes.models.Resp_revision;
import mx.unam.sa.ponentes.repository.DocumentoRepo;
import mx.unam.sa.ponentes.repository.RespEvaluadorRepo;
import mx.unam.sa.ponentes.utils.Utils;


@Service
public class RespEvaluadorService {
    @Autowired
    RespuestaService respuestaService;


    @Autowired
    RespEvaluadorRepo respEvaluadorRepo;

    @Autowired
    DocumentoService documentoService;

    @Autowired
    DocumentoRepo documentoRepo;

    /**
     * Guarda la respuesta del evaluador y del responsable del proyecto
     * 
     * @param idRespCuestionario
     * @param observaciones
     * @param fecha
     * @param Username
     * @param files
     * @param statusRespuesta    1 respuesta del evaluador
     *                           2 respuesta del responsable del proyecto
     *                           3 sólo para comité de evaluación
     *                           4 respuesta final del evaluador
     * @param statusCuestionario 1 captura
     *                           2 evaluado
     *                           3 revisado
     *                           4 Terminado
     */
    @Transactional
    public Resp_revision saveRespEvaluador(Long idRespCuestionario, String observaciones, Date fecha, String username,
            MultipartFile[] files, int statusRespuesta, int statusCuestionario) {

        try {

            RespCuestionario respCuestionario = respuestaService.findRespCuestionarioById(idRespCuestionario);
            
            Set<Documento> doctos = new HashSet<>();

            if (files != null) {
                for (MultipartFile file : files) {
                    Documento documento = new Documento();
                    documento.setNombre(file.getOriginalFilename());
                    documento.setTipo(file.getContentType());
                    documento.setSize((int) file.getSize());
                    documento.setData(file.getBytes());
                    doctos.add(documento);
                }
            }

            Resp_revision respEvaluador = new Resp_revision();
            respEvaluador.setRespCuestionario(respCuestionario);
            respEvaluador.setObservaciones(observaciones);
            respEvaluador.setFecParaRespuesta(fecha);
            respEvaluador.setUser(username);
            respEvaluador.setNumRespuesta(1);
            respEvaluador.setStatus(statusRespuesta);
            respEvaluador.setDoctos(doctos);

            // Cambia el estatus de la respuesta a revisado, estatus 3

            if (statusCuestionario == 3) {
                respCuestionario.setStatus(statusCuestionario);
                respCuestionario.setFechaRevisado(new Date()); // Tampoco se actualiza la fecha de revisión
            }else if (statusCuestionario == 4) {
                respCuestionario.setStatus(statusCuestionario);
                respCuestionario.setFechaTerminado(new Date());
            }

            respEvaluador = respEvaluadorRepo.save(respEvaluador);

            return respEvaluador;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    
    public Resp_revision saveRespEvaluador(Long idRespCuestionario, String observaciones, String username) {

        try {
            RespCuestionario respCuestionario = respuestaService.findRespCuestionarioById(idRespCuestionario);
            
            Resp_revision respEvaluador = new Resp_revision();
            respEvaluador.setRespCuestionario(respCuestionario);
            respEvaluador.setObservaciones(observaciones);
            respEvaluador.setUser(username);
            respEvaluador.setNumRespuesta(1);
            respEvaluador.setStatus(1);
            respEvaluador = respEvaluadorRepo.save(respEvaluador);

            return respEvaluador;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<RespEvaluadorDTO> findByRespCuestionarioStatus_1_2_4(Long idRespCuestionario) {
        List<RespEvaluadorDTO> salida = null;

        List<Integer> statusList = List.of(1, 2, 4);
        List<Resp_revision> observaciones = respEvaluadorRepo
                .findByRespCuestionarioIdRespCuestionarioAndStatusInOrderByFecReg(idRespCuestionario, statusList);

        if (observaciones != null && !observaciones.isEmpty()) {
            salida = observaciones.stream().map(observacion -> {
                RespEvaluadorDTO dto = new RespEvaluadorDTO();
                dto.setIdresp_evaluador(observacion.getIdresp_evaluador());
                dto.setIdRespCuestionario(idRespCuestionario);
                dto.setObservaciones(observacion.getObservaciones());
                dto.setFecParaRespuesta(Utils.formatFecha(observacion.getFecParaRespuesta()));
                dto.setFecReg(Utils.formatFechaHM(observacion.getFecReg()));
                dto.setStatus(observacion.getStatus());

                List<Map<String, String>> documentos = documentoService.getDoctosMapWeb(observacion.getDoctos());
                

                dto.setDocumentos(documentos);

                return dto;
            }).toList();
        }
        return salida;
    }

    public List<RespEvaluadorDTO> findByRespCuestionario(Long idRespCuestionario) {
        List<RespEvaluadorDTO> salida = null;
        List<Resp_revision> observaciones = respEvaluadorRepo
                .findByRespCuestionarioIdRespCuestionarioOrderByFecReg(idRespCuestionario);

        if (observaciones != null && !observaciones.isEmpty()) {
            salida = observaciones.stream().map(observacion -> {
                RespEvaluadorDTO dto = new RespEvaluadorDTO();
                dto.setIdresp_evaluador(observacion.getIdresp_evaluador());
                dto.setIdRespCuestionario(idRespCuestionario);
                dto.setObservaciones(observacion.getObservaciones());
                dto.setFecParaRespuesta(Utils.formatFecha(observacion.getFecParaRespuesta()));
                dto.setFecReg(Utils.formatFechaHM(observacion.getFecReg()));
                dto.setStatus(observacion.getStatus());

                List<Map<String, String>> documentos = documentoService.getDoctosMapWeb(observacion.getDoctos());
                

                dto.setDocumentos(documentos);

                return dto;
            }).toList();
        }
        return salida;
    }

    public List<DocumentoDTO> getRelacionDoctosRubrica(Long idRespCuestionario) {
        List<Integer> idDoctos = documentoRepo.getRelacionDoctosRub(idRespCuestionario);
        List<DocumentoDTO> documentos = documentoRepo.getDatosDocto(idDoctos.toArray(new Integer[idDoctos.size()]));
        return documentos;
    }

}
