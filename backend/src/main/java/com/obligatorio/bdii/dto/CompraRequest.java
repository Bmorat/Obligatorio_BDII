package com.obligatorio.bdii.dto;

public record CompraRequest(
    String paisDocUsuario,
    String tipoDocUsuario,
    String numeroDocUsuario,
    Integer idEvento,
    Integer idEstadio,
    String tipo, //sector :Tribuna, Platea, Palco
    Integer cantidad
) {}
