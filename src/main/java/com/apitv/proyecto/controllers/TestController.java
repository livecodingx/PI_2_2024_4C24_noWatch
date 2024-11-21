package com.apitv.proyecto.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @GetMapping("/")

    public String testFree() {
        return "This is a public endpoint. No authentication is required.";
    }

    @GetMapping("/user")

    public Principal user(Principal user) {
        return user;
    }
}
