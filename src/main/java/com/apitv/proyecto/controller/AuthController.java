package com.apitv.proyecto.controller;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public AuthController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/login/google")
    public RedirectView redirectToGoogle(HttpServletRequest request) {
        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        OAuth2AuthorizationRequest authorizationRequest = resolver.resolve(request, "google");

        return new RedirectView(authorizationRequest.getAuthorizationRequestUri());
    }
}
