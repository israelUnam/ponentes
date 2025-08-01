package mx.unam.sa.ponentes.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.unam.sa.ponentes.config.Datosconf;

@Controller
public class LoginController {

    Datosconf datosconf;

    public LoginController(Datosconf datosconf) {
        this.datosconf = datosconf;
    }

    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal OidcUser principal) {


        model.addAttribute("facultad", datosconf.getFacultad());

        if (principal != null) {
            return "redirect:/";
        }
        return "login";
    }
}
