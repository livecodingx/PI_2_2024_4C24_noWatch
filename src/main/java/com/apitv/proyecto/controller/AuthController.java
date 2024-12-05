package com.apitv.proyecto.controller;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.service.GoogleTokenDecoder;
import com.apitv.proyecto.service.JwtService;
import com.apitv.proyecto.service.UsuarioService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "**")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final GoogleTokenDecoder googleTokenDecoder;

    public AuthController(JwtService jwtService, UsuarioService usuarioService, GoogleTokenDecoder googleTokenDecoder) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.googleTokenDecoder = googleTokenDecoder;
    }

    @PostMapping("/callback/google")
    public Map<String, String> googleCallback(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");
        Map<String, String> response = new HashMap<>();

        if (idTokenString == null) {
            logger.error("Token is missing");
            response.put("error", "Token is missing");
            return response;
        }

        try {
            // Decodificar el idToken y verificar audiencia y expiración
            logger.info("Decodificando el token...");
            String expectedAudience = "93151703613-9oa78lt5h7lf7gimrnlluciag49pi4hn.apps.googleusercontent.com";
            Claims claims = googleTokenDecoder.decodeAndVerifyBasicToken(idTokenString, expectedAudience);

            // Extraer información del usuario del idToken decodificado
            logger.info("Extrayendo información del token...");
            String email = claims.get("email", String.class);
            String nombre = claims.get("given_name", String.class);
            String apellido = claims.get("family_name", String.class);
            String fotoUrl = claims.get("picture", String.class);

            // Verificar si la información extraída es válida
            if (email == null || nombre == null) {
                logger.error("Error: No se pudo extraer la información del usuario del token");
                response.put("error", "Invalid token content");
                return response;
            }

            // Guardar información del usuario en la base de datos si no existe
            logger.info("Verificando si el usuario existe en la base de datos...");
            Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
            if (usuario == null) {
                logger.info("Usuario no encontrado, creando uno nuevo...");
                usuario = new Usuario();
                usuario.setCorreo(email);
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setFotoUrl(fotoUrl);
                usuarioService.guardarUsuario(usuario);
                logger.info("Usuario creado y guardado en la base de datos");
            } else {
                logger.info("Usuario ya existente: " + email);
            }

            // Generar el token JWT
            logger.info("Generando el token JWT para el usuario...");
            String jwtToken = jwtService.generateJwtToken(email, nombre);
            response.put("token", jwtToken);
            response.put("message", "Authentication successful");
            logger.info("Token JWT generado exitosamente");
            return response;

        } catch (Exception e) {
            logger.error("Error decoding or verifying idToken: " + e.getMessage());
            response.put("error", "Error decoding or verifying idToken: " + e.getMessage());
            return response;
        }
    }
}
