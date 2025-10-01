package mx.unam.sa.ponentes.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.dto.CuestDetalleDTO;
import mx.unam.sa.ponentes.models.User;
import mx.unam.sa.ponentes.repository.UserRepository;
import mx.unam.sa.ponentes.service.CuestionarioService;
import mx.unam.sa.ponentes.utils.Utils;

@Controller
@RequestMapping("/terminado")
@PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
public class TerminadoController {
    @Autowired
    private CuestionarioService cuestionarioService;
    @Autowired
    Datosconf datosconf;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/listterminado")
    public String home(String param, String anio, Boolean terminado, Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("picture", principal.getAttribute("picture"));
        model.addAttribute("avisoprivacidad", datosconf.getAvisoprivacidad());

        if (terminado){
            model.addAttribute("status", 4);
        } else {
            model.addAttribute("status", 5);    
        }
        

        User user = userRepository.findByUsername(principal.getAttribute("email"))
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));  

        Boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("admin", isAdmin);        

        if (anio == null) {
            anio = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }

        Map<String, Object> params = Utils.getMapDecode(param);
        Long idCuestionario = Long.parseLong((String) params.get("idCuestionario"));

        int status = 4;
        if(!terminado){
            status = 5;
        }
        

        List<CuestDetalleDTO> detalles = cuestionarioService.getDetalleCuestionarioAnio(idCuestionario, status,
                "revision", user.getUsername() , true, Integer.parseInt(anio));

        model.addAttribute("tipo", status);
        model.addAttribute("detalles", detalles);

        List<Integer> anios = cuestionarioService.getDistinctYears(idCuestionario);

        model.addAttribute("anios", anios);
        model.addAttribute("currentYear", anio);
        model.addAttribute("titulo", (String) params.get("titulo"));
        model.addAttribute("subtitulo", (String) params.get("subtitulo"));

        return new String("terminado/listterminado");
    }
}
