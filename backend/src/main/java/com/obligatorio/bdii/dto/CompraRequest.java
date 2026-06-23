package com.obligatorio.bdii.dto;

import com.obligatorio.bdii.model.SectorTipo;

public record CompraRequest(
    String paisDocUsuario,
    String tipoDocUsuario,
    String numeroDocUsuario,
    Integer idEvento,
    Integer idEstadio,
    SectorTipo tipo, //sector: A, B, C, D
    Integer cantidad
) {}
