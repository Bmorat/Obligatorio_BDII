package com.obligatorio.bdii.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obligatorio.bdii.dto.EventoReporteDTO;
import com.obligatorio.bdii.dto.MayorCompradorDTO;
import com.obligatorio.bdii.service.ReporteService;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/eventos-mas-vendidos")
    public List<EventoReporteDTO> eventosMasVendidos() {
        return reporteService.eventosMasVendidos();
    }

    @GetMapping("/mayores-compradores")
    public List<MayorCompradorDTO> mayoresCompradores() {
        return reporteService.mayoresCompradores();
    }
}
