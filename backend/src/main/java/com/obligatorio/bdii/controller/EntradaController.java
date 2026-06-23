package com.obligatorio.bdii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.obligatorio.bdii.dto.ValidarEntradaRequest;
import com.obligatorio.bdii.model.Entrada;
import com.obligatorio.bdii.service.EntradaService;


@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    private final EntradaService entradaService;
    
    public EntradaController(EntradaService entradaService){
        this.entradaService = entradaService;
    }

    @GetMapping
    public List<Entrada> obtenerEntradasPorUsuario (
        @RequestParam String paisDoc,
        @RequestParam String tipoDoc,
        @RequestParam String numeroDoc) {
            return entradaService.obtenerEntradasPorUsuario(paisDoc,tipoDoc,numeroDoc);
        }
    
    @PostMapping("/validar")
    public ResponseEntity<?> validarEntrada(@RequestBody ValidarEntradaRequest request) {
        return entradaService.validarEntrada(request);
    }

    @PostMapping("/{idEntrada}/renovar-qr")
    public ResponseEntity<?> renovarQR(@PathVariable Integer idEntrada) {
        return entradaService.renovarQR(idEntrada);
    }

    

}
