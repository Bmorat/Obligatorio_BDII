package com.obligatorio.bdii.controller;

import org.springframework.web.bind.annotation.PostMapping;

public class LoginController {
    @PostMapping("/api/helloworld")
    public String helloWorld() {
        return "helloworld";
    }
}
