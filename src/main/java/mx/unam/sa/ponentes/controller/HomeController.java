package mx.unam.sa.ponentes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.unam.sa.ponentes.entity.User;
import mx.unam.sa.ponentes.service.OAuth2UserService;

import jakarta.transaction.Transactional;

@Controller
public class HomeController {
    @Autowired
    private OAuth2UserService oAuth2UserService;

    @GetMapping("/")
    @Transactional
    public String home(Model model,  @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("title", "Project Manager");
        model.addAttribute("message", "Bienvenido al Sistema de Gestión de Proyectos");

        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            User user = oAuth2UserService.getUserByUsername(principal.getAttribute("email"));
            user.getAuthorities().forEach(authority -> {
                model.addAttribute("authority", authority.getAuthority());
            });
        }

        return "home";
    }


 
} 