package com.obligatorio.bdii.controller;

import com.obligatorio.bdii.model.SeHabilita;
import com.obligatorio.bdii.model.SectorTipo;
import com.obligatorio.bdii.service.seHablitaService;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/SeHablita")
public class SeHablitaController {

    private final seHablitaService seHablitaService;

    public SeHablitaController(seHablitaService seHablitaService) {
        this.seHablitaService = seHablitaService;
    }

    @PostMapping
    public ResponseEntity<?> setSeHablitado(@RequestParam Integer IdEvento, @RequestParam SectorTipo Tipo, @RequestParam Integer Precio, @RequestParam Integer CapacidadMax) {
        try {
            boolean result = seHablitaService.setSeHablitado(IdEvento, Tipo, Precio, CapacidadMax);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))
                return ResponseEntity.badRequest().body(Map.of("error", "El sector ya esta habilitado para este evento"));
            if (e.getMessage() != null && e.getMessage().contains("Cannot add or update a child row"))
                return ResponseEntity.badRequest().body(Map.of("error", "El sector no existe para este estadio"));
            throw e;
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSeHablitado(@RequestParam("IdEvento") Integer idEvento, @RequestParam("Tipo") SectorTipo tipo) {
        try {
            boolean result = seHablitaService.deleteSeHablitado(idEvento, tipo);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se puede deshabilitar: el sector tiene entradas vendidas"));
        }
    }

    @DeleteMapping("/forzar")
    public ResponseEntity<?> forceDeleteSeHablitado(@RequestParam("IdEvento") Integer idEvento, @RequestParam("Tipo") SectorTipo tipo) {
        boolean result = seHablitaService.forceDeleteSeHablitado(idEvento, tipo);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{idEvento}")
    public List<SeHabilita> getSeHablita(@PathVariable Integer idEvento) {
    return seHablitaService.getSeHablita(idEvento);
    }
}
