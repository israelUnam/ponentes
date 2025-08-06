package mx.unam.sa.ponentes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.dto.CuestionariosRespDTO;
import mx.unam.sa.ponentes.dto.SolicitudDTOImp;
import mx.unam.sa.ponentes.models.CatContenido;
import mx.unam.sa.ponentes.models.Cuestionario;
import mx.unam.sa.ponentes.models.Documento;
import mx.unam.sa.ponentes.models.Notificacion;
import mx.unam.sa.ponentes.models.RespCuestionario;
import mx.unam.sa.ponentes.models.Respuesta;
import mx.unam.sa.ponentes.models.User;
import mx.unam.sa.ponentes.repository.UserRepository;
import mx.unam.sa.ponentes.service.CatContenidoService;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.service.DocumentoService;
import mx.unam.sa.ponentes.service.NotificacionService;
import mx.unam.sa.ponentes.service.RegisterService;
import mx.unam.sa.ponentes.service.RespuestaService;
import mx.unam.sa.ponentes.utils.Utils;

@Slf4j
@Controller
@RequestMapping("/cuestionario")
public class CuestionarioController {
    private final CuestionarioService cuestionarioService;
    private final CatContenidoService catContenidoService;
    private final NotificacionService notificaService;
    private final UserRepository userRepository;
    private final RespuestaService respuestaService;
    private final DocumentoService documentoService;
    private final RegisterService registerService;
    private final Datosconf datosconf;

    public CuestionarioController(CuestionarioService cuestionarioService, CatContenidoService catContenidoService,
            NotificacionService notificaService, UserRepository userRepository, RespuestaService respuestaService, 
            DocumentoService documentoService, RegisterService registerService, Datosconf datosconf) {
        this.cuestionarioService = cuestionarioService;
        this.catContenidoService = catContenidoService;
        this.notificaService = notificaService;
        this.userRepository = userRepository;
        this.respuestaService = respuestaService;
        this.documentoService = documentoService;
        this.registerService = registerService;
        this.datosconf = datosconf;
    }

    @GetMapping("/solicitud")
    public String solicitud(@Valid String idCuestionario, Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        model.addAttribute("fallo", false);
        try {
            model.addAttribute("idCuestionario", idCuestionario);
            Long id_Cuestionario = Long.parseLong(Utils.decode(idCuestionario));
            Cuestionario cuestionario = cuestionarioService.findById(id_Cuestionario);
            model.addAttribute("cuestionario", cuestionario);
            List<SolicitudDTOImp> preguntas = catContenidoService.findCuestionario(id_Cuestionario);

            Map<String, String> condiciones = catContenidoService.getChkCondicion(preguntas);
            model.addAttribute("checks_Required", condiciones.get("checks_Required"));

            model.addAttribute("preguntas", preguntas);

            return new String("cuestionario/solicitud");
        } catch (Exception e) {
            model.addAttribute("mensaje", "cuestionario no encontrado");
            return new String("/error");
        }
    }

    @PostMapping("/guardasolicitud")
    public String guardaSolicitud(HttpServletRequest request, @RequestParam("files") MultipartFile[] files,
            Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));
        model.addAttribute("fallo", false);

        User user = userRepository.findByUsername(principal.getAttribute("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // En caso de error se regresan las preguntas con el valor aceptado
        List<SolicitudDTOImp> preguntas = null;
        List<Respuesta> respuestas = new ArrayList<>();

        Long idCuestionario = null;
        try {
            model.addAttribute("idCuestionario", request.getParameter("idCuestionario"));
            // Este valor siempre va a estar presente
            idCuestionario = Long.parseLong(Utils.decode(request.getParameter("idCuestionario")));
            Cuestionario cuestionario = cuestionarioService.findById(idCuestionario);
            preguntas = catContenidoService.findCuestionario(idCuestionario);
            Map<String, String> condiciones = catContenidoService.getChkCondicion(preguntas);
            model.addAttribute("checks_Required", condiciones.get("checks_Required"));

            // Areglo para identificar la posición de los archivos
            List<Integer> idFiles = new ArrayList<>();

            for (int i = 0; i < preguntas.size(); i++) {
                SolicitudDTOImp solicitud = preguntas.get(i);

                Respuesta respuesta = new Respuesta();

                CatContenido contenido = catContenidoService.findById(solicitud.getIdContenido());
                respuesta.setContenido(contenido);

                if (solicitud.getDesCatalogo().equals("Texto")) {
                    String textResp = request.getParameter("text_" + solicitud.getIdPregunta());
                    respuesta.setRespuesta(textResp);
                    solicitud.setRespuesta(textResp);
                } else if (solicitud.getDesCatalogo().equals("Multiple")) {
                    // En un control inputText asociado se guarda el valor de la respuesta, ese se
                    // recupra

                    String textCheck = request
                            .getParameter("chk_" + solicitud.getIdPregunta() + "_" + solicitud.getIdContenido());

                    if (solicitud.getOtro()) {
                        String respAdicional = request
                                .getParameter("otro_" + solicitud.getIdPregunta() + "_" + solicitud.getIdContenido());
                        respuesta.setResp_adicional(respAdicional);
                        solicitud.setResp_adicional(respAdicional);
                    }

                    respuesta.setRespuesta(textCheck);
                    solicitud.setRespuesta(textCheck);

                } else if (solicitud.getDesCatalogo().equals("Una")) {
                    String RespRadio = request.getParameter("optResp_" + solicitud.getIdPregunta());
                    if (RespRadio.equals(solicitud.getIdContenido().toString())) {
                        respuesta.setRespuesta("1");
                        solicitud.setRespuesta("1");

                        if (solicitud.getOtro()) {
                            String respAdicional = request.getParameter(
                                    "optotro_" + solicitud.getIdPregunta() + "_" + solicitud.getIdContenido());
                            respuesta.setResp_adicional(respAdicional);
                            solicitud.setResp_adicional(respAdicional);
                        }
                    } else {
                        respuesta.setRespuesta("0");
                        solicitud.setRespuesta("0");
                    }

                } else if (solicitud.getDesCatalogo().equals("Archivo")) {
                    idFiles.add(i);
                } else if (solicitud.getDesCatalogo().equals("SI")) {
                    respuesta.setRespuesta("Si");
                    solicitud.setRespuesta("Si");
                }

                respuestas.add(respuesta);
            }

            for (MultipartFile file : files) {
                if (file.getSize() > 4000000) {
                    throw new Exception("El archivo " + file.getOriginalFilename() + " excede el tamaño permitido");
                }
            }

            int i = 0;
            for (MultipartFile file : files) {
                Documento documento = new Documento();
                documento.setData(file.getBytes());
                documento.setNombre(file.getOriginalFilename());
                documento.setTipo(file.getContentType());
                documento.setSize((int) file.getSize());
                respuestas.get(idFiles.get(i)).setDocumento(documento);
                preguntas.get(idFiles.get(i)).setRespuesta(file.getOriginalFilename());
                i++;
            }

            RespCuestionario respCuestionario = cuestionarioService.saveNuevasRespuestas(idCuestionario, respuestas,
                    cuestionario.getClaveCuestionario(), user);

            Notificacion notificacion = new Notificacion();
            notificacion.setNotificacion(
                    "Se ha realizado una solicitud de cuestionario con folio: " + respCuestionario.getFolio());
            notificacion.setIdUser(user.getId());
            notificacion.setUrlDestino("/cuestionario/listsolicitud?param=" + Utils.encode(idCuestionario.toString()));
            notificacion.setIdCuestionario(idCuestionario);
            notificaService.save(notificacion);

            String param = Utils
                    .encode("idRespCuestionario=" + respCuestionario.getIdRespCuestionario() + "&acceso=usuario");

            return new String("redirect:/cuestionario/capturado?param=" + param);
        } catch (Exception e) {
            // Se envia a la vista de captura, pues hubo fallo
            model.addAttribute("fallo", true);
            if (idCuestionario != null) {
                try {
                    // Lee los datos del cuestionario para la vista de captura
                    Cuestionario cuestionario = cuestionarioService.findById(idCuestionario);
                    model.addAttribute("cuestionario", cuestionario);
                } catch (Exception e2) {
                    model.addAttribute("mensaje", "cuestionario no encontrado");
                    return new String("/error");
                }

                // Vuelve a enviar las preguntas
                if (preguntas != null) {
                    model.addAttribute("preguntas", preguntas);
                } else {
                    model.addAttribute("mensaje", "Fallo al recuperar las preguntas");
                    return new String("/error");
                }
            } else {
                model.addAttribute("mensaje", "Fallo al recuperar el cuestionario");
                return new String("/error");
            }
            model.addAttribute("mensaje", e.getMessage());
            return new String("/cuestionario/solicitud");
        }
    }

    @GetMapping("/capturado")
    public String getCapturado(@RequestParam String param, Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        try {
            Map<String, Object> entrada = Utils.getMapDecode(param);
            Long idResp = Long.parseLong(entrada.get("idRespCuestionario").toString());
            String acceso = entrada.get("acceso").toString();

            Map<String, Object> consulta = respuestaService.getRespuestas(idResp);

            @SuppressWarnings("unchecked")
            List<SolicitudDTOImp> respuestas = (List<SolicitudDTOImp>) consulta.get("preguntasyResp");

            model.addAttribute("idRespCuestionario", idResp);
            model.addAttribute("folio", consulta.get("folio"));
            model.addAttribute("titulo", consulta.get("titulo").toString());
            model.addAttribute("subtitulo", consulta.get("subtitulo").toString());
            model.addAttribute("respuestas", respuestas);
            model.addAttribute("acceso", acceso);
            model.addAttribute("status", consulta.get("status"));

            String parametro = Utils.encode(consulta.get("idCuestionario").toString());
            model.addAttribute("parametro", parametro);
            return new String("cuestionario/capturado");
        } catch (Exception e) {
            return new String("/error");
        }

    }

    @GetMapping("/listsolicitud")
    public String getSolicitudes(@RequestParam String param, Model model,
            @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        try {
            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long idCuestionario = Long.parseLong(Utils.decode(param));

            List<CuestionariosRespDTO> cuestionariosResp = respuestaService
                    .getCuestionariosResp(user.getId(), idCuestionario);

            cuestionariosResp.stream().forEach(c -> {
                c.setParametros(
                        Utils.encode("idRespCuestionario=" + c.getIdRespCuestionario().toString() + "&acceso=usuario"));
            });

            model.addAttribute("cuestionariosResp", cuestionariosResp);

            return new String("cuestionario/listsolicitud");
        } catch (Exception e) {
            return new String("/error");
        }

    }

    @PostMapping("/reemplazaArchivo")
    @Transactional
    public ResponseEntity<HashMap<String, Object>> reemplazaArchivo(HttpServletRequest request,
            @RequestParam("files") MultipartFile[] files, String param, @AuthenticationPrincipal OAuth2User principal) {

        Long idRespCuestionario = Long.parseLong(Utils.getMapDecode(param).get("idRespCuestionario").toString());
        Integer idDocto = Integer.parseInt(Utils.getMapDecode(param).get("idDocto").toString());
        Long idRespuesta = Long.parseLong(Utils.getMapDecode(param).get("idRespuesta").toString());
        Long idContenido = Long.parseLong(Utils.getMapDecode(param).get("idContenido").toString());
        String extension = "pdf";
        try {

            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String parametros = null;
            Respuesta respuesta = respuestaService.findByRespuestasAndContenido(idRespuesta, idRespCuestionario,
                    idContenido);

            String nomDoctoAnt = respuesta.getDocumento().getNombre();

            int iddoctoResp = respuesta.getDocumento().getIdDocto();
            log.info("idDocto: " + idDocto + " respuesta.idDocto: " + iddoctoResp);
            
            if (iddoctoResp == idDocto) {
                Documento documento = new Documento();
                documento.setIdDocto(idDocto);
                documento.setData(files[0].getBytes());

                String nomDocto = files[0].getOriginalFilename();
                if (nomDocto != null && nomDocto.length() > 300) {
                    // recortar el nombre del documento a partir de la derecha para mantener la
                    // extensión
                    nomDocto = nomDocto.substring(nomDocto.length() - 300);
                }

                documento.setNombre(nomDocto);
                documento.setTipo(files[0].getContentType());
                documento.setSize((int) files[0].getSize());
                documento.setUser(user);
                documentoService.save(documento);

                String registro = "Reemplazo de documento: " + nomDoctoAnt + " por " + nomDocto + " en respuesta: "
                        + respuesta.getIdRespuesta();

                user.getId();
                registerService.saveRegister(Integer.toString(iddoctoResp), request.getRemoteAddr(), registro);

                Map<String, Object> map = new HashMap<>();
                map.put("idDocto", respuesta.getDocumento().getIdDocto());

                
                if (nomDocto != null ) {
                    extension = nomDocto.substring(nomDocto.lastIndexOf(".") + 1);
                }
                
                parametros = Utils.encodeWJT("docto", map, datosconf.getSecretJWT());

            } else {
                throw new Exception("El documento no coincide con la respuesta");
            }

            final String paramSalida = parametros;
            final String extensionFinal = extension;

            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("param", paramSalida);
                    put("extension", extensionFinal);
                    put("file", files[0].getOriginalFilename());
                    put("mensaje", "Exito");
                    put("status", 1);
                }
            });

        } catch (Exception e) {
            log.error(e.getMessage());
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

}
