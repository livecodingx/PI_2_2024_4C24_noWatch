package com.apitv.proyecto.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GoogleTokenDecoder {
    public Claims decodeToken(String idToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Error al decodificar el idToken: " + e.getMessage());
        }
    }

    public Claims decodeAndVerifyBasicToken(String idToken, String expectedAudience) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

            if (!expectedAudience.equals(claims.getAudience())) {
                throw new RuntimeException("Invalid audience");
            }

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new RuntimeException("Token expired");
            }

            return claims;

        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el idToken: " + e.getMessage());
        }
    }
}
