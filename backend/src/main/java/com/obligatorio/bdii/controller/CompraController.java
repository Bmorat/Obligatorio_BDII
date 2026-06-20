package com.obligatorio.bdii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.obligatorio.bdii.dto.CompraRequest;
import com.obligatorio.bdii.service.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {
    
    private final CompraService compraService;

    public CompraController(CompraService compraService){
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<?> comprar(@RequestBody CompraRequest request){
       return compraService.comprar(request);
    }
}
