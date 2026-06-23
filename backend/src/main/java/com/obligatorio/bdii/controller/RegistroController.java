package com.obligatorio.bdii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.obligatorio.bdii.dto.RegistroRequest;
import com.obligatorio.bdii.service.RegistroService;

@RestController
@RequestMapping("/api/auth/registro")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService){
        this.registroService = registroService;
    }
    
    @PostMapping
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request){
        return registroService.registrar(request);
    }
}
