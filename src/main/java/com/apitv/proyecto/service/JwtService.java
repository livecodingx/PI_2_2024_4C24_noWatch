package com.apitv.proyecto.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String SECRET = "SecreteKey";  // Clave secreta
    private final SecretKey SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS512.getJcaName());

    // Genera un JWT para el usuario autenticado
    public String generateJwtToken(OAuth2User oAuth2User) {
        return Jwts.builder()
                .setSubject(oAuth2User.getAttribute("email"))  // El correo como identificador
                .claim("name", oAuth2User.getAttribute("name")) // Información adicional
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512) // Firmar con la clave secreta
                .compact();
    }


    // validar el JWT
    public boolean validateJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()  // Usamos parserBuilder() en lugar de parser()
                    .setSigningKey(SECRET_KEY)        // Usamos SecretKey en lugar de String
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Si el token es válido, podemos trabajar con los claims (datos del token)
            return true;
        } catch (Exception e) {
            // Token inválido o expirado
            return false;
        }
    }
}
