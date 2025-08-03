package mx.unam.sa.ponentes.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.dto.SolicitudDTOImp;
import mx.unam.sa.ponentes.models.Cuestionario;
import mx.unam.sa.ponentes.service.CatContenidoService;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/cuestionario")
public class CuestionarioController {
    private final CuestionarioService cuestionarioService;
    private final CatContenidoService catContenidoService;
    
    public CuestionarioController(CuestionarioService cuestionarioService, CatContenidoService catContenidoService) {
        this.cuestionarioService = cuestionarioService;
        this.catContenidoService = catContenidoService;
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
}
