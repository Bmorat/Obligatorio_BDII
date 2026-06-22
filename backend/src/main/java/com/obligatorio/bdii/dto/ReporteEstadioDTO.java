package com.obligatorio.bdii.dto;

import java.util.List;

public record ReporteEstadioDTO(
    Integer idEstadio,
    String nombreEstadio,
    List<EventoReporteDTO> eventos
) {}