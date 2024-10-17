package com.apitv.proyecto.controller;

import com.apitv.proyecto.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthCallbackController {

    private final JwtService jwtService;

    public AuthCallbackController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/callback/google")
    public ResponseEntity<?> handleGoogleCallback(
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
            OidcUser oidcUser) {

        // Generar un JWT usando JwtService con la información del usuario de Google
        String jwtToken = jwtService.generateJwtToken(oidcUser);

        // Devolver el JWT como respuesta JSON
        return ResponseEntity.ok(jwtToken);
    }
}
