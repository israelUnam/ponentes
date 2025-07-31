package mx.unam.sa.ponentes.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            return "redirect:/";
        }
        return "login";
    }
}
