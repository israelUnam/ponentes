package mx.unam.sa.ponentes.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mx.unam.sa.ponentes.service.RegisterService;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RegisterService registerService;

    public CustomLogoutSuccessHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null) {
            String username = authentication.getName();
            String localAddress = request.getLocalAddr();
            try {
                registerService.saveRegister(username, localAddress, "salida");
            } catch (Exception e) {
                System.out.println("Error saving register of exit: " + e.getMessage() + " for user: " + username + " from IP: "
                        + localAddress);
            }

        } else {
            log.info("Logout success for anonymous user");
        }
        response.sendRedirect("/login?logout");
    }
}
