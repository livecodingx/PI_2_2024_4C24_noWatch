package com.apitv.proyecto.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @GetMapping("/testfree")
    public String freeAcess() {
        return "Acceso libre, sin autentificacion necesaria";
    }

    @GetMapping("/testsecure")
    public String secureAccess() {
        return "Acceso privado, autenticacion requerida";
    }
}
