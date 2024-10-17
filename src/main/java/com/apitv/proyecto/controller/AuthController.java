package com.apitv.proyecto.controller;

import com.apitv.proyecto.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // creacion de endpoints
    // endpoint de autenticacion con google
    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        // Spring Boot maneja la redirección automáticamente usando OAuth2
        response.sendRedirect("/oauth2/authorization/google");
    }
    @Autowired
    private JwtService jwtService;

    @GetMapping("/login-success")
    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String jwtToken = jwtService.generateJwtToken(oAuth2User);
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("email", oAuth2User.getAttribute("email"));
        response.put("name", oAuth2User.getAttribute("name"));
        return ResponseEntity.ok(response);
    }

}
