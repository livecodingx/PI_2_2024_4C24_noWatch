package com.apitv.proyecto.controller;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.service.JwtService;
import com.apitv.proyecto.services.UsuarioServicio;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioServicio usuarioService;

    public AuthController(JwtService jwtService, UsuarioServicio usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/callback/google")
    public Map<String, String> googleCallback(OAuth2AuthenticationToken token) {
        Map<String, String> response = new HashMap<>();

        if (token == null) {
            response.put("error", "Authentication information is missing or invalid");
            return response;
        }

        // Guardar información del usuario en la base de datos
        String email = token.getPrincipal().getAttribute("email");
        String nombre = token.getPrincipal().getAttribute("given_name");
        String apellido = token.getPrincipal().getAttribute("family_name");
        String fotoUrl = token.getPrincipal().getAttribute("picture");

        Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setFotoUrl(fotoUrl);
            usuarioService.guardarUsuario(usuario);
        }

        // Generar el token JWT
        String jwtToken = jwtService.generateJwtToken(token.getPrincipal());
        response.put("token", jwtToken);
        response.put("message", "Authentication successful");
        return response;
    }
}
