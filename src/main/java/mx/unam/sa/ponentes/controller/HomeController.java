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
import mx.unam.sa.ponentes.dto.NotificacionDTO;
import mx.unam.sa.ponentes.models.User;
import mx.unam.sa.ponentes.repository.UserRepository;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.service.NotificacionService;
import mx.unam.sa.ponentes.utils.Utils;

@Controller
public class HomeController {
    private CuestionarioService cuestionarioService;
    private Datosconf datosconf;
    private UserRepository userRepository;
    private NotificacionService notificacionService;

    public HomeController(Datosconf datosconf, CuestionarioService cuestionarioService, UserRepository userRepository,
            NotificacionService notificacionService) {
        this.datosconf = datosconf;
        this.cuestionarioService = cuestionarioService;
        this.userRepository = userRepository;
        this.notificacionService = notificacionService;
    }

    @GetMapping("/")
    @Transactional
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());
            model.addAttribute("facultad", datosconf.getFacultad());

            List<CuestionarioDTO> cuestionarios = cuestionarioService.getAllCuestionarios();

            User user = userRepository.findByUsername(principal.getAttribute("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            try {
                cuestionarios.stream().forEach(c -> {
                    List<NotificacionDTO> notificaciones = notificacionService.findByIdCuestionarioAndidUserActual(
                            c.getId(),
                            user.getId());
                    c.setNotificacion(notificaciones);
                    c.setParametros(Utils.encode(c.getId().toString()));
                    c.setTotalNotificaciones(notificacionService.countNotificaciones(user.getId(), c.getId()));
                });
            } catch (Exception e) {
                System.out.println("Error al obtener notificaciones: " + e.getMessage());
            }

            model.addAttribute("cuestionarios", cuestionarios);
        }

        return "home";
    }

    @GetMapping("/contacto")
    @Transactional
    public String contacto(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("facultad", datosconf.getFacultad());
        }

        return "contacto";
    }

    @GetMapping("/fallo")
    @Transactional
    public String fallo(String error, Integer status, Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("error", error);
            model.addAttribute("status", status);
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("facultad", datosconf.getFacultad());
        }

        return "error";
    }


}