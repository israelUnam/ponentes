package mx.unam.sa.ponentes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.dto.CuestionariosRespDTO;
import mx.unam.sa.ponentes.dto.SolicitudDTOImp;
import mx.unam.sa.ponentes.models.Cuestionario;
import mx.unam.sa.ponentes.models.RespCuestionario;
import mx.unam.sa.ponentes.models.Respuesta;
import mx.unam.sa.ponentes.repository.RespCuestionarioRepo;
import mx.unam.sa.ponentes.repository.RespuestaRep;
import mx.unam.sa.ponentes.utils.Utils;


@Service
public class RespuestaService {
    @Autowired
    RespCuestionarioRepo respCuestionarioRep;

    @Autowired
    CatContenidoService contenidoService;

    @Autowired
    RespuestaRep respuestaRep;


    public Map<String, Object> getRespuestas(Long idRespCuestionario)
            throws RuntimeException {
        Map<String, Object> salida = new HashMap<>();

        List<SolicitudDTOImp> preguntasyResp = null;
        try {
            RespCuestionario respCuestionario = respCuestionarioRep.findById(idRespCuestionario).get();
            Cuestionario cuestionario = respCuestionario.getCuestionario();
            salida.put("titulo", cuestionario.getTitulo());
            salida.put("subtitulo", cuestionario.getSubtitulo());
            salida.put("idCuestionario", cuestionario.getIdCuestionario());
            salida.put("folio", respCuestionario.getFolio());
            salida.put("status", respCuestionario.getStatus());
            salida.put("userRespCuestionario", respCuestionario.getUser().getUsername());

            // Todavía no tiene la respuesta
            preguntasyResp = contenidoService.findCuestionario(cuestionario.getIdCuestionario());

            preguntasyResp.stream().forEach(p -> {
                Respuesta respuesta = respuestaRep.findByContenidoIdContenidoAndRespCuestionarioIdRespCuestionario(
                        p.getIdContenido(),
                        idRespCuestionario);
                if (respuesta != null) {
                    if (respuesta.getContenido().getDescripcion().equals("Archivo")) {
                        if (respuesta.getDocumento() != null && respuesta.getDocumento().getSize() > 0) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("idDocto", respuesta.getDocumento().getIdDocto());
                            String param = Utils.encodeWJT("docto", map, "AxRwYWESR");

                            //p.setRespuesta("<a href=\"/documento/getPdf?param=" + param + "\" target='_blank'>"
                            //        + respuesta.getDocumento().getNombre() + "</a>");
                            
                            p.setRespuesta( param + "&" + respuesta.getDocumento().getNombre());

                            String parametros = "idDocto=" + respuesta.getDocumento().getIdDocto()
                                    + "&idRespCuestionario=" + idRespCuestionario
                                    + "&idRespuesta=" + respuesta.getIdRespuesta()      
                                    + "&idContenido=" + respuesta.getContenido().getIdContenido();
                            p.setIdDocto(Utils.encode(parametros));
                            p.setNomDocto(respuesta.getDocumento().getNombre());
                        }else{
                            p.setRespuesta("sin documento");
                        }
                    } else {
                        p.setRespuesta(respuesta.getRespuesta());
                        p.setResp_adicional(respuesta.getResp_adicional());
                    }
                }

            });
            salida.put("preguntasyResp", preguntasyResp);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las respuestas");
        }
        return salida;
    }

    public List<RespCuestionario> getRespuestasByUser(Integer idUser, Long idCuestionario) {
        return respCuestionarioRep.findByUserIdAndCuestionarioIdCuestionario(idUser, idCuestionario);
    }

    public RespCuestionario getRespuestaCuestionarioByUser(Integer idUser, Long idCuestionarioResp) {
        
        return respCuestionarioRep.findByUserIdAndIdRespCuestionario(idUser, idCuestionarioResp);
    }

    @Transactional
    public List<CuestionariosRespDTO> getCuestionariosResp(Integer idUser, Long idCuestionario) {
        List<CuestionariosRespDTO> cuestionariosResp = new ArrayList<>();
        
        List<RespCuestionario> respCuestionarios = this.getRespuestasByUser(idUser, idCuestionario);

        respCuestionarios.stream().forEach(r -> {

            if (r.getStatus() != 0) {
                CuestionariosRespDTO c = new CuestionariosRespDTO();
                c.setIdRespCuestionario(r.getIdRespCuestionario());
                c.setCuestionario(r.getCuestionario().getTitulo() + "-" + r.getCuestionario().getSubtitulo());
                c.setUsuario(r.getUser().getUsername());
                c.setFechaSolicitud(Utils.formatFecha(r.getFecReg()));
                if (r.getStatus() == 1) {
                    c.setEstado("Captura inicial");
                } else if (r.getStatus() == 2) {
                    c.setEstado("En revisión");
                } else if (r.getStatus() == 3) {
                    c.setEstado("En recomendación");
                } else if (r.getStatus() == 4) {
                    c.setEstado("Terminado");
                } else {
                    c.setEstado("Otro");
                }
                c.setFolio(r.getFolio());

                cuestionariosResp.add(c);
            }
        });
        return cuestionariosResp;
    }

    public void deleteRespuesta(Long idRespCuestionario) {
        respCuestionarioRep.deleteById(idRespCuestionario);
    }

    public RespCuestionario findRespCuestionarioById(Long idRespCuestionario) {
        return respCuestionarioRep.findById(idRespCuestionario).get();
    }

    public RespCuestionario saveRespCuestionario(RespCuestionario respCuestionario) {
        return respCuestionarioRep.save(respCuestionario);
    }

    public Respuesta findByRespuestasAndContenido(Long idRespuesta, Long idRespCuestionario, Long idContenido){
        return respuestaRep.findByIdRespuestaAndRespCuestionarioIdRespCuestionarioAndContenidoIdContenido(idRespuesta, idRespCuestionario, idContenido);
    }
}
