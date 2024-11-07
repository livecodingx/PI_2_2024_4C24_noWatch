package com.apitv.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping("/testfree")
    public String testFree() {
        return "This is a public endpoint. No authentication is required.";
    }

    @GetMapping("/testsecure")
    public String testSecure() {
        return "This is a secure endpoint. You must be authenticated to access this.";
    }
}
