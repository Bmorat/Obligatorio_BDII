package com.obligatorio.bdii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import com.obligatorio.bdii.service.TransferenciaService;
import com.obligatorio.bdii.dto.TransferenciaRequest;
import com.obligatorio.bdii.model.Transferencia;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {
    
    private final  TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService){
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public ResponseEntity<?> iniciarTransferencia(@RequestBody TransferenciaRequest request){
        return transferenciaService.iniciarTransferencia(request);
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptarTransferencia(@PathVariable Integer id){
        return transferenciaService.aceptarTransferencia(id);
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarTransferencia(@PathVariable Integer id){
        return transferenciaService.rechazarTransferencia(id);
    }

    @GetMapping("/recibidas")
    public List<Transferencia> obtenerTransferenciasRecibidas(
        @RequestParam String paisDoc,
        @RequestParam String tipoDoc,
        @RequestParam String numeroDoc){
            return transferenciaService.obtenerTransferenciasRecibidas(paisDoc, tipoDoc, numeroDoc);
        }
    
}
