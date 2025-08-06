package mx.unam.sa.ponentes.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.dto.CuesEvaluaDTO;
import mx.unam.sa.ponentes.dto.CuestDetalleDTO;
import mx.unam.sa.ponentes.repository.RespCuestionarioRepo;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.utils.Utils;

@Controller
@RequestMapping("/evalua")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class EvaluaController {

    private final CuestionarioService cuestionarioService;
    private final RespCuestionarioRepo respCuestionarioRepo;
    private final Datosconf datosconf;

    public EvaluaController(CuestionarioService cuestionarioService, RespCuestionarioRepo respCuestionarioRepo,
            Datosconf datosconf) {
        this.cuestionarioService = cuestionarioService;
        this.respCuestionarioRepo = respCuestionarioRepo;
        this.datosconf = datosconf;
    }

    @RequestMapping("/listcuestionarios")
    public String evalua(Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        List<CuesEvaluaDTO> cuestionarios = null;

        cuestionarios = cuestionarioService.getCuestionariosListEvaluacion();

        cuestionarios.stream().forEach(c -> {
            c.setCapturados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 1));
            c.setEvaluados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 2));
            c.setDictamenincompleto(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 3));
            c.setTerminados(respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(c.getId(), 4));
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

        Map<String, Object> params = Utils.getMapDecode(param);

        Long idCuestionario = Long.parseLong((String) params.get("idCuestionario"));
        int status = -1;
        if (tipo.equals("captura")) {
            status = 1;
        } else if (tipo.equals("evalua")) {
            status = 2;
        }
        List<CuestDetalleDTO> detalles = cuestionarioService.getDetalleCuestionario(idCuestionario, status,
                "obs_evaluador_revision",  true);

        model.addAttribute("tipo", status);
        model.addAttribute("detalles", detalles);
        model.addAttribute("titulo", (String) params.get("titulo"));
        model.addAttribute("subtitulo", (String) params.get("subtitulo"));
        
        return "evalua/listdetalle";
    }

}
