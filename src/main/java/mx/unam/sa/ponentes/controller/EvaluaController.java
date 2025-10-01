package mx.unam.sa.ponentes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.dto.ComentariosDTO;
import mx.unam.sa.ponentes.dto.CuesEvaluaDTO;
import mx.unam.sa.ponentes.dto.CuestDetalleDTO;
import mx.unam.sa.ponentes.dto.DocumentoDTO;
import mx.unam.sa.ponentes.dto.SolicitudDTOImp;
import mx.unam.sa.ponentes.models.ComentariosSimple;
import mx.unam.sa.ponentes.models.Documento;
import mx.unam.sa.ponentes.models.User;
import mx.unam.sa.ponentes.repository.ComentariosSimpleRepo;
import mx.unam.sa.ponentes.repository.RespCuestionarioRepo;
import mx.unam.sa.ponentes.repository.UserRepository;
import mx.unam.sa.ponentes.service.ComentarioService;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.service.DocumentoService;
import mx.unam.sa.ponentes.service.RespEvaluadorService;
import mx.unam.sa.ponentes.service.RespuestaService;
import mx.unam.sa.ponentes.utils.Utils;

@Controller
@RequestMapping("/evalua")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class EvaluaController {

    private final CuestionarioService cuestionarioService;
    private final RespCuestionarioRepo respCuestionarioRepo;
    private final Datosconf datosconf;
    private final UserRepository userRepository;
    private final RespuestaService respuestaService;
    private final RespEvaluadorService respEvaluadorService;
    private final ComentarioService comentarioService;
    private final ComentariosSimpleRepo comentariosSimpleRepo;
    private final DocumentoService documentoService;

    public EvaluaController(CuestionarioService cuestionarioService, RespCuestionarioRepo respCuestionarioRepo,
            Datosconf datosconf, UserRepository userRepository, RespuestaService respuestaService,
            RespEvaluadorService respEvaluadorService, ComentarioService comentarioService,
            ComentariosSimpleRepo comentariosSimpleRepo, DocumentoService documentoService) {
        this.cuestionarioService = cuestionarioService;
        this.respCuestionarioRepo = respCuestionarioRepo;
        this.datosconf = datosconf;
        this.userRepository = userRepository;
        this.respuestaService = respuestaService;
        this.respEvaluadorService = respEvaluadorService;
        this.comentarioService = comentarioService;
        this.comentariosSimpleRepo = comentariosSimpleRepo;
        this.documentoService = documentoService;
    }

    @RequestMapping("/listcuestionarios")
    public String evalua(Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        User user = userRepository.findByUsername(principal.getAttribute("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Boolean isEval = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EVAL"));

        model.addAttribute("isEval", isEval);

        List<CuesEvaluaDTO> cuestionarios = null;
        cuestionarios = cuestionarioService.getCuestionariosListEvaluacion();

        cuestionarios.stream().forEach(c -> {
            c.setCapturados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 1));
            c.setEvaluados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 2));
            c.setAvalados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 4));
            c.setNoAvalados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 5));
            c.setParam(Utils.encode("idCuestionario=" + c.getId() + "&titulo=" + c.getTitulo() + "&subtitulo="
                    + c.getSubtitulo()));
        });

        model.addAttribute("cuestionarios", cuestionarios);
        return "evalua/listcuestionarios";
    }

    @RequestMapping("/listdetalle")
    public String listdetalle(String param, String tipo, Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        User user = userRepository.findByUsername(principal.getAttribute("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        Boolean isEval = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EVAL"));

        model.addAttribute("admin", isAdmin);

        Map<String, Object> params = Utils.getMapDecode(param);

        Long idCuestionario = Long.parseLong((String) params.get("idCuestionario"));
        int status = -1;
        if (tipo.equals("captura")) {
            status = 1;
        } else if (tipo.equals("evalua")) {
            status = 2;
        }

        List<CuestDetalleDTO> detalles = cuestionarioService.getDetalleCuestionario(idCuestionario, status,
                "obs_evaluador_revision", true);

        model.addAttribute("tipo", status);
        model.addAttribute("detalles", detalles);
        model.addAttribute("titulo", (String) params.get("titulo"));
        model.addAttribute("subtitulo", (String) params.get("subtitulo"));
        model.addAttribute("isEval", isEval);

        return "evalua/listdetalle";
    }

    /**
     * Cambia el status de captura a revisión
     * 
     * @param param codificado con el idCuestionario
     * @return
     */
    @RequestMapping(value = "/cambiaStatusCaptura", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> cambiaStatusCaptura(@RequestParam String param) {
        try {
            Map<String, Object> params = Utils.getMapDecode(param);
            Long idCuestionario = Long.parseLong((String) params.get("idCuestionario"));

            // List<RespCuestionario> ids =
            // respCuestionarioRepo.findByCuestionarioIdCuestionarioAndStatus(idCuestionario,
            // 1);
            cuestionarioService.cambiaStatusCaptura(idCuestionario, 1, 2);

            /*
             * ids.stream().forEach(resp -> {
             * notificacionService
             * .save(new Notificacion("El cuestionario " + resp.getFolio() +
             * " ha sido enviado a revisión",
             * "/cuestionario/listsolicitud?param="
             * + Utils.encode(resp.getCuestionario().getIdCuestionario().toString()),
             * resp.getUser().getId(), idCuestionario));
             * });
             */

            int capturados = respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(idCuestionario, 1);
            int evaluados = respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(idCuestionario, 2);

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("capturados", capturados);
                    put("evaluados", evaluados);
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

    @RequestMapping(value = "/guardaComentario", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> guardaComentario(@RequestParam String param,
            @RequestParam String comentario,
            @RequestParam Long idTema, @AuthenticationPrincipal OAuth2User principal) {

        User user = userRepository.findByUsername(principal.getAttribute("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());

            ComentariosSimple comentarioSimple = comentariosSimpleRepo
                    .findByidUserAndIdRespCuestionarioAndIdTema(user.getId(), idResp, idTema);
            if (comentarioSimple == null) {
                comentarioSimple = new ComentariosSimple();
            } else {
                comentarioSimple.setFecReg(new java.util.Date());
            }

            comentarioSimple.setComentario(comentario);
            comentarioSimple.setIdRespCuestionario(idResp);
            comentarioSimple.setIdTema(idTema);
            comentarioSimple.setIdUser(user.getId());

            comentarioSimple = comentariosSimpleRepo.save(comentarioSimple);
            String fecha = Utils.formatFechaHM(comentarioSimple.getFecReg());

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("fechaAct", fecha);
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

    @RequestMapping(value = "/recuperaComentario", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> recuperaComentario(@RequestParam String param,
            @RequestParam Long idTema, @AuthenticationPrincipal OAuth2User principal) {

        System.out.println("recuperaComentario: Param: " + param + " idTema: " + idTema);
        try {

            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());

            ComentariosSimple comentarioSimple = comentariosSimpleRepo
                    .findByidUserAndIdRespCuestionarioAndIdTema(user.getId(), idResp, idTema);

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("comentario", comentarioSimple == null ? "" : comentarioSimple.getComentario());
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

    @RequestMapping(value = "/recuperaOtrosComentarios", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> recuperaOtrosComentarios(@RequestParam String param,
            @RequestParam Long idTema, @AuthenticationPrincipal OAuth2User principal) {

        try {

            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());

            List<ComentariosDTO> comentarios = comentarioService.getComentariosOtrosUsuarios(idResp, idTema,
                    user.getId(), user.getUsername());

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("comentarios", comentarios);
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

    @PostMapping("/cambiarecomendacion")
    public ResponseEntity<HashMap<String, Object>> cambiaRecomendacion(@RequestParam String param,
            @AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> entrada = Utils.getMapDecode(param);

        User user = userRepository.findByUsername(principal.getAttribute("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Boolean isEval = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EVAL"));

        try {
            if (isEval) {
                Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());
                System.out.println("Id resp cuestionario: " + idResp);
                respuestaService.cambiarStatusRespuesta(idResp, 2);
            }

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", "Exito");
                    put("status", 1);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String error = e.getMessage();

            final String errorSalida = error;
            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", errorSalida);
                    put("status", 2);
                }
            });

        }

    }

    @RequestMapping(value = "/evaluasolicitud")
    public String evaluasolicitud(@RequestParam String param, Model model,
            @AuthenticationPrincipal OAuth2User principal) {

        try {

            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());

            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());
            String acceso = entrada.get("acceso").toString();

            Map<String, Object> consulta = respuestaService.getRespuestas(idResp);
            String titulo = consulta.get("titulo").toString();
            String subtitulo = consulta.get("subtitulo").toString();
            String folio = consulta.get("folio").toString();

            String userRespCuestionario = consulta.get("userRespCuestionario").toString();

            @SuppressWarnings("unchecked")
            List<SolicitudDTOImp> respuestas = (List<SolicitudDTOImp>) consulta.get("preguntasyResp");

            List<Long> temas = respuestas.stream()
                    .map(SolicitudDTOImp::getIdTema)
                    .distinct()
                    .collect(Collectors.toList());

            String idtemas = temas.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            List<DocumentoDTO> doctos = respEvaluadorService.getRelacionDoctosRubrica(idResp);

            doctos.stream().forEach(doctoDto -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idDocto", doctoDto.getIdDocto());
                doctoDto.setParam(Utils.encodeWJT("docto", map, datosconf.getSecretJWT()));
            });

            // Este permite identificar si el usuario actual (evaluador) es el mismo que
            // capturó el cuestionario
            model.addAttribute("userRespCuestionario", userRespCuestionario);
            model.addAttribute("folio", folio);
            model.addAttribute("doctos", doctos);
            model.addAttribute("acceso", acceso);
            model.addAttribute("idtemas", idtemas);
            model.addAttribute("idRespCuestionario", idResp);
            model.addAttribute("titulo", titulo);
            model.addAttribute("subtitulo", subtitulo);
            model.addAttribute("respuestas", respuestas);
            model.addAttribute("externo", false);

            String parametro = Utils.encode("idRespCuestionario=" + idResp.toString());
            model.addAttribute("parametro", parametro);

            return "evalua/evaluasolicitud";
        } catch (Exception e) {
            return new String("/error");
        }

    }

    @RequestMapping(value = "/borraRubrica", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> borraRubrica(@RequestParam String param,
            @AuthenticationPrincipal OAuth2User principal) {

        try {

            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Boolean isEval = user.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_EVAL"));

            if (!isEval) {
                throw new RuntimeException("No tienes permisos para borrar este documento");
            }

            Map<String, Object> map = Utils.decodeJWT("docto", param, datosconf.getSecretJWT());
            int idDocto = (int) map.get("idDocto");

            Documento documento = documentoService.findDocumentobyId(idDocto);

            if (documento == null) {
                throw new RuntimeException("Documento no encontrado");
            }

            if (!documento.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("No tienes permisos para borrar este documento");
            }

            documentoService.deleteRubricaByIdDocto(idDocto);

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", "Exito");
                    put("status", 1);
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", e.getMessage());
                    put("status", 2);
                }
            });
        }
    }

    @PostMapping("/guardarTerminacion")
    public ResponseEntity<HashMap<String, Object>> guardarTerminacion(
            @RequestParam String param,
            @RequestParam String observaciones,
            @RequestParam String aval,
            @AuthenticationPrincipal OAuth2User principal) {

        try {
            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Boolean isEval = user.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_EVAL"));

            if (!isEval) {
                throw new RuntimeException("No tienes permisos para realizar esta acción");
            }

            // Decode param to get idRespCuestionario
            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());

            // Update status based on aval value
            int nuevoStatus = "1".equals(aval) ? 4 : 5; // 4=Avalado, 5=No avalado

            // Save observations and update status
            respuestaService.cambiarStatusRespuesta(idResp, nuevoStatus);

            // Save evaluator observations
            respEvaluadorService.saveRespEvaluador(idResp, observaciones, user.getUsername());


            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", "Terminación guardada exitosamente");
                    put("status", 1);
                }
            });

        } catch (Exception e) {
            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", "Error: " + e.getMessage());
                    put("status", 2);
                }
            });
        }
    }

}
