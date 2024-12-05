package com.apitv.proyecto.filter;

import com.apitv.proyecto.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Component
@CrossOrigin(origins = "http://localhost:3000")

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Autowired
    private JwtService jwtService;

    // Constructor sin inyección de `JwtService` para evitar la referencia circular
    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        logger.info("Filtering request: " + request.getRequestURI());
        // Omitir las rutas de autenticación
        String path = request.getRequestURI();
        if (path.startsWith("/auth/")) {
            chain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // Verificar si el encabezado contiene el token JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warning("Authorization header is missing or invalid");
            chain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7); // Omitir "Bearer "
        userEmail = jwtService.extractUsername(jwtToken); // Extraer el email del token

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(jwtToken, userEmail)) {
                logger.info("Token is valid for user: " + userEmail);
                UserDetails user = User.withUsername(userEmail).password("").authorities(new ArrayList<>()).build();
                var authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warning("Token is invalid for user: " + userEmail);
            }
        }
        chain.doFilter(request, response);
    }
}