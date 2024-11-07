package com.apitv.proyecto.config;

import com.apitv.proyecto.filter.JwtAuthenticationFilter;
import com.apitv.proyecto.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());
    private final JwtService jwtService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtService jwtService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtService = jwtService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/", "/login", "/testfree", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(authenticationSuccessHandler())  // Añadir el success handler personalizado
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Agregar el filtro de JWT
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                org.springframework.security.core.Authentication authentication)
                    throws IOException, ServletException {
                if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                    String jwtToken = jwtService.generateJwtToken((OAuth2User) oauthToken.getPrincipal());

                    logger.info("Authentication successful, generated JWT: " + jwtToken);

                    // Redirigir o enviar el token en la respuesta
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }
}
