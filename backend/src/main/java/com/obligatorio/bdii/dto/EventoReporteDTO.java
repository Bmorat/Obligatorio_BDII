package com.obligatorio.bdii.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventoReporteDTO(
    Integer idEvento,
    LocalDate fecha,
    LocalTime hora,
    Integer totalEntradas,
    BigDecimal totalRemunerado,
    List<SectorReporteDTO> sectores
) {}