package com.apitv.proyecto.controller;

import com.apitv.proyecto.models.entities.Favorito;
import com.apitv.proyecto.service.FavoritoService;
import com.apitv.proyecto.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.apitv.proyecto.service.UsuarioService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "http://localhost:80", allowedHeaders = "*")

public class FavoritoController {
    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public FavoritoController(FavoritoService favoritoService, JwtService jwtService, UsuarioService usuarioService) {
        this.favoritoService = favoritoService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Favorito> obtenerFavoritos(@RequestHeader("Authorization") String authHeader) {
        try {
            // Obtener el token JWT del header
            String jwt = authHeader.substring(7); // Omitir "Bearer "
            String correo = jwtService.extractUsername(jwt); // Extraer el correo del token
            Long idUsuario = usuarioService.obtenerIdPorCorreo(correo); // Obtener el ID del usuario
            return favoritoService.obtenerFavoritosPorUsuario(idUsuario); // Buscar favoritos por ID de usuario
        } catch (Exception e) {
            // Manejo de errores si ocurre un problema con la autenticación o el JWT
            return new ArrayList<>(); // Devuelve una lista vacía si hay un error
        }
    }


    @PostMapping
    public ResponseEntity<String> agregarFavorito(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> body) {
        String jwt = authHeader.substring(7); // Omitir "Bearer "
        String correo = jwtService.extractUsername(jwt); // Extraer el correo del token
        Long idUsuario = usuarioService.obtenerIdPorCorreo(correo); // Obtener el ID del usuario

        String urlCanal = body.get("url");

        boolean favoritoGuardado = favoritoService.agregarFavorito(idUsuario, urlCanal);
        if (favoritoGuardado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Canal añadido a favoritos");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El canal ya está en favoritos");
        }
    }


    @DeleteMapping("/{id}")
    public Map<String, String> eliminarFavorito(@PathVariable Long id) {
        favoritoService.eliminarFavorito(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Favorito eliminado con éxito");
        return response;
    }
}
