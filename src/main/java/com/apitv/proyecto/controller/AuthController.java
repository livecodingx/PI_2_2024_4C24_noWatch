package com.apitv.proyecto.controller;

import com.apitv.proyecto.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/callback/web/google")
    public void googleCallback(OAuth2AuthenticationToken token, HttpServletResponse response) throws IOException {
        String jwtToken = jwtService.generateJwtToken(token.getPrincipal());
        logger.info("Generando JWT: " + jwtToken);

        // Redirigir al frontend con el token JWT como parámetro
        String redirectUrl = "http://localhost:3000/web/perfil?token=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }
}
