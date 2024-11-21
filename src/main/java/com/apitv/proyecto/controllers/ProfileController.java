package com.apitv.proyecto.controller;

import com.apitv.proyecto.service.JwtService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final JwtService jwtService;

    public ProfileController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public Map<String, Object> profile(@AuthenticationPrincipal User user) {
        Map<String, Object> userProfile = new HashMap<>();
        String email = user.getUsername();
        userProfile.put("email", email);
        userProfile.put("first_name", "Usuario"); // Puedes ajustar esto si tienes más información en el token
        userProfile.put("last_name", "Desconocido"); // Puedes ajustar esto si tienes más información en el token
        userProfile.put("photo", "URL_foto"); // Puedes obtener la foto desde otra fuente si es necesario

        return userProfile;
    }
}
