package com.obligatorio.bdii.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.obligatorio.bdii.dto.CompraRequest;
import com.obligatorio.bdii.model.Compra;
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

    @GetMapping
    public List<Compra> obtenerComprasPorUsuario(
        @RequestParam String paisDoc,
        @RequestParam String tipoDoc,
        @RequestParam String numeroDoc){
            return compraService.obtenerComprasPorUsuario(paisDoc,tipoDoc,numeroDoc);
        }
    
}
