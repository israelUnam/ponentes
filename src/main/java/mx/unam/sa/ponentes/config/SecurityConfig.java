package mx.unam.sa.ponentes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {

        private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
        private final CustomOauth2SuccessHandler CustomOauth2SuccessHandler;
        private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.trace("Configuring http filterChain");
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "/css/**", "/js/**", "/images/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(
                                                oauth2 -> oauth2.userInfoEndpoint(infoEndpoint -> infoEndpoint
                                                                .userService(oAuth2UserService))
                                                                .successHandler(CustomOauth2SuccessHandler)
                                                                .loginPage("/login"))
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login??logout")
                                                .logoutSuccessHandler(customLogoutSuccessHandler)
                                                .logoutRequestMatcher(
                                                                request -> "/logout".equals(request.getRequestURI()) &&
                                                                                "GET".equalsIgnoreCase(
                                                                                                request.getMethod()))
                                                .permitAll());
                return http.build();
        }

}
