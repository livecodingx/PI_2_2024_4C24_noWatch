package com.apitv.proyecto.controller;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.service.JwtService;
import com.apitv.proyecto.service.UsuarioService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String CLIENT_ID = "93151703613-9oa78lt5h7lf7gimrnlluciag49pi4hn.apps.googleusercontent.com";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/callback/google")
    public Map<String, String> googleCallback(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");
        Map<String, String> response = new HashMap<>();

        if (idTokenString == null) {
            response.put("error de idtoken", "Token is missing");
            return response;
        }

        try {
            logger.info("Decodificando el token...");
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                logger.info("Extrayendo información del token...");

                // Extraer información del usuario del payload
                String email = payload.getEmail();
                String nombre = (String) payload.get("given_name");
                String apellido = (String) payload.get("family_name");
                String fotoUrl = (String) payload.get("picture");

                // Guardar información del usuario en la base de datos si no existe
                logger.info("Verificando si el usuario existe en la base de datos...");
                Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setCorreo(email);
                    usuario.setNombre(nombre);
                    usuario.setApellido(apellido);
                    usuario.setFotoUrl(fotoUrl);
                    usuarioService.guardarUsuario(usuario);
                    logger.info("Usuario creado y guardado en la base de datos");
                }

                // Generar el token JWT
                logger.info("Generando el token JWT para el usuario...");
                String jwtToken = jwtService.generateJwtToken(email, nombre);
                response.put("token", jwtToken);
                response.put("message", "Authentication successful");
                logger.info(jwtToken);
            } else {
                response.put("error", "Invalid ID token");
            }

            return response;
        } catch (Exception e) {
            response.put("error", "Error decoding or verifying idToken: " + e.getMessage());
            return response;
        }
    }
}
