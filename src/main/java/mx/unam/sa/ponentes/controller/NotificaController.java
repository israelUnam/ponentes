package mx.unam.sa.ponentes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mx.unam.sa.ponentes.dto.NotificacionDTO;
import mx.unam.sa.ponentes.models.Cuestionario;
import mx.unam.sa.ponentes.models.User;
import mx.unam.sa.ponentes.repository.UserRepository;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.service.NotificacionService;
import mx.unam.sa.ponentes.utils.Utils;

@Controller
@RequestMapping("/notificaciones")
public class NotificaController {
    private NotificacionService notificaService;
    private UserRepository userRepository;
    private CuestionarioService cuestionarioService;

    NotificaController(NotificacionService notificaService, UserRepository userRepository,
            CuestionarioService cuestionarioService) {
        this.notificaService = notificaService;
        this.userRepository = userRepository;
        this.cuestionarioService = cuestionarioService;
    }

    @RequestMapping("/notifica")
    public String notifica(@RequestParam String param, Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));

        User user = userRepository.findByUsername(principal.getAttribute("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long idCuestionario = Long.parseLong(Utils.decode(param));

        Cuestionario cuestionario = cuestionarioService.findById(idCuestionario);
        model.addAttribute("titulo", cuestionario.getTitulo());
        model.addAttribute("subtitulo", cuestionario.getSubtitulo());

        List<NotificacionDTO> notificaciones = notificaService.findByIdUserAndidCuestionario(user.getId(),
                idCuestionario);
        model.addAttribute("notificaciones", notificaciones);

        return "notificaciones/notifica";
    }

    @RequestMapping(value = "/quitaRevisa", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map<String, Object>> quitaRevisa(@RequestParam String param) {
        try {
            Long idNotifica = Long.parseLong(Utils.decode(param));
            notificaService.quitaRevisa(idNotifica);
            return ResponseEntity.ok().body(new HashMap<String, Object>() {
                {
                    put("mensaje", "Exito");
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
