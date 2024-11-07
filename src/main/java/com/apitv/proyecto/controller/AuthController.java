package com.apitv.proyecto.controller;

import com.apitv.proyecto.service.JwtService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/callback/google")
    public Map<String, String> googleCallback(@AuthenticationPrincipal OAuth2User oAuth2User) {
        logger.info("Handling Google callback");
        Map<String, String> response = new HashMap<>();

        if (oAuth2User == null) {
            logger.warning("Authentication information is missing or invalid");
            response.put("error", "Authentication information is missing or invalid");
            return response;
        }

        logger.info("Generating JWT for authenticated user: " + oAuth2User.getAttribute("email"));
        String jwtToken = jwtService.generateJwtToken(oAuth2User);

        response.put("token", jwtToken);
        response.put("message", "Authentication successful");
        return response;
    }
}
