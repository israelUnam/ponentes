package mx.unam.sa.ponentes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.unam.sa.ponentes.dto.RespEvaluadorDTO;
import mx.unam.sa.ponentes.service.RespEvaluadorService;
import mx.unam.sa.ponentes.utils.Utils;

@RestController
@RequestMapping("/recomendacionRest")
public class RecomiendaRestController {

    @Autowired
    RespEvaluadorService respEvaluadorService;

    @PostMapping("/getObservaciones")
    public ResponseEntity<Map<String, Object>> getObservaciones(String param) {
        try {
            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());

            List<RespEvaluadorDTO> respEvaluador = respEvaluadorService.findByRespCuestionario(idResp);

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("respEvaluador", respEvaluador);
                    put("mensaje", "exito");
                    put("status", 1);
                }
            });
        } catch (Exception e) {
            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", e.getMessage());
                    put("status", 0);
                }
            });
        }
    }
}
