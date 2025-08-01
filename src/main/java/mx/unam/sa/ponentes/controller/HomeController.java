package mx.unam.sa.ponentes.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.dto.CuestionarioDTO;
import mx.unam.sa.ponentes.service.CuestionarioService;

@Controller
public class HomeController {
    private CuestionarioService cuestionarioService;
    private Datosconf datosconf;

    public HomeController(Datosconf datosconf, CuestionarioService cuestionarioService) {
        this.datosconf = datosconf;
        this.cuestionarioService = cuestionarioService;
    }

    @GetMapping("/")
    @Transactional
    public String home(Model model,  @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());
            model.addAttribute("facultad", datosconf.getFacultad());
           
            List<CuestionarioDTO> cuestionarios = cuestionarioService.getAllCuestionarios();
            model.addAttribute("cuestionarios", cuestionarios);

        }

        return "home";
    }

    @GetMapping("/contacto")
    @Transactional
    public String contacto(Model model,  @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("facultad", datosconf.getFacultad());
        }

        return "contacto";
    }
 
} 