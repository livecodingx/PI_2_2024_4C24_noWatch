package com.apitv.proyecto.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "SecretKeySecretKeySecretKeySecretKey"; // Clave secreta de al menos 32 caracteres
    private static final long TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hora
    private static final Logger logger = Logger.getLogger(JwtService.class.getName());

    private final Key key;

    public JwtService() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateJwtToken(OAuth2User oAuth2User) {
        logger.info("Generating JWT for user: " + oAuth2User.getAttribute("email"));
        return Jwts.builder()
                .setSubject(oAuth2User.getAttribute("email"))
                .claim("name", oAuth2User.getAttribute("name"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        logger.info("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.info("Extracting claim from token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logger.info("Extracting all claims from token");
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String userEmail) {
        logger.info("Validating token for user: " + userEmail);
        final String username = extractUsername(token);
        return (username.equals(userEmail) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        logger.info("Checking if token is expired");
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        logger.info("Extracting expiration date from token");
        return extractClaim(token, Claims::getExpiration);
    }
}