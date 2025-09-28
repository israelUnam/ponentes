package mx.unam.sa.ponentes.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mx.unam.sa.ponentes.models.Documento;
import mx.unam.sa.ponentes.service.DocumentoService;
import mx.unam.sa.ponentes.utils.Utils;


@RestController
@PreAuthorize("hasAnyRole('ROLE_EVAL', 'ROLE_ADMIN')")
@RequestMapping("/evaluarest")
public class EvaluaRestController {
    @Autowired
    DocumentoService documentoService;

    @RequestMapping(value = "/guardaRubrica", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> guardaRubrica(@RequestParam String param,
            @RequestParam("files") MultipartFile[] files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuario = authentication.getName();

        try {
            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());

            Documento documento = documentoService.saveRubrica(files[0], idResp, usuario);

            String paramDocto = Utils.encodeWJT("docto", new HashMap<String, Object>() {
                {
                    put("idDocto", documento.getIdDocto());
                }
            }, "AxRwYWESR");

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("idFile", documento.getIdDocto());
                    put("filename", documento.getNombre());
                    put("user", usuario);
                    put("paramDocto", paramDocto);
                    put("mensaje", "Exito");
                    put("status", 1);
                }
            });

        } catch (Exception e) {
            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", e.getMessage());
                    put("status", 2);
                }
            });
        }
    }

    // @ExceptionHandler(MaxUploadSizeExceededException.class)
    // public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {

    //     return ResponseEntity.ok().body("El archivo excede el límite permitido");
    // }
}
