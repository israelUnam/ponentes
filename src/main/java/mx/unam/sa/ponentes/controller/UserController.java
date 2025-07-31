package mx.unam.sa.ponentes.controller;

import mx.unam.sa.ponentes.dto.AdditionalDetailsDto;
import mx.unam.sa.ponentes.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final OAuth2UserService oAuth2UserService;

    @GetMapping("/home")
    public String sayHello(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/complete-registration")
    public String showCompleteRegistrationForm(Model model) {
        model.addAttribute("additionalDetails", new AdditionalDetailsDto());
        return "registration-completion";
    }

    @PostMapping("/complete-registration")
    public String completeRegistration(@ModelAttribute AdditionalDetailsDto additionalDetails, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            oAuth2UserService.completeRegistration(username, additionalDetails);
            redirectAttributes.addFlashAttribute("message", "Registration completed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to complete registration: " + e.getMessage());
        }

        return "redirect:/home";
    }
    
}
