package mx.unam.sa.ponentes.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mx.unam.sa.ponentes.service.RegisterService;

@Component
@Slf4j
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {

    RegisterService registerService;

    CustomOauth2SuccessHandler (RegisterService registerService) {
        this.registerService = registerService;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       
        String username = authentication.getName();
        String localAddress = request.getLocalAddr();
        try {
            registerService.saveRegister(username, localAddress, "ingreso");    
        } catch (Exception e) {
            System.out.println("Error saving register: " + e.getMessage() + " for user: " + username + " from IP: " + localAddress);
        }

        try {
            response.sendRedirect("/");
            log.info("Redirecting to /");
        } catch (Exception e) {
            log.error("Error during redirecting to /", e);
            throw e;
        }
    }
}
