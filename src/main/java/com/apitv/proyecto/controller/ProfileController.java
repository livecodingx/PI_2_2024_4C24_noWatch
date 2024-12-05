package com.apitv.proyecto.controller;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.service.JwtService;
import com.apitv.proyecto.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public ProfileController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user")
    public Map<String, Object> profile(HttpServletRequest request) {
        Map<String, Object> userProfile = new HashMap<>();
        String authHeader = request.getHeader("Authorization");

        // Verificar si el encabezado de autorización está presente y es válido
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            userProfile.put("error", "Authorization header is missing or invalid");
            return userProfile;
        }

        String jwtToken = authHeader.substring(7); // Omitir "Bearer "
        try {
            // Verificar si el token es válido
            if (!jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken))) {
                userProfile.put("error", "Token is invalid or expired");
                return userProfile;
            }

            // Extraer el correo del token
            String email = jwtService.extractUsername(jwtToken);

            // Obtener al usuario desde la base de datos
            Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
            if (usuario != null) {
                userProfile.put("email", usuario.getCorreo());
                userProfile.put("first_name", usuario.getNombre());
                userProfile.put("last_name", usuario.getApellido());
                userProfile.put("photo", usuario.getFotoUrl());
            } else {
                userProfile.put("error", "Usuario no encontrado");
            }

        } catch (Exception e) {
            userProfile.put("error", "Error procesando el token: " + e.getMessage());
        }

        return userProfile;
    }
}
