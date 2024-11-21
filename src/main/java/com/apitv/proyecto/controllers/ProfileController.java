package com.apitv.proyecto.controllers;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.services.UsuarioServicio;
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

    private final UsuarioServicio usuarioServicio;

    public ProfileController(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("")
    public Map<String, Object> profile(@AuthenticationPrincipal User user) {
        Map<String, Object> userProfile = new HashMap<>();
        String email = user.getUsername();

        // Obtener el usuario de la base de datos
        Usuario usuario = usuarioServicio.obtenerUsuarioPorCorreo(email);
        if (usuario != null) {
            userProfile.put("email", usuario.getEmail());
            userProfile.put("first_name", usuario.getNombre());
            userProfile.put("last_name", usuario.getApellido());
            userProfile.put("photo", usuario.getFotoUrl());
        } else {
            userProfile.put("error", "Usuario no encontrado");
        }

        return userProfile;
    }
}
