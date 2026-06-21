package com.obligatorio.bdii.dto;

public record TransferenciaRequest(
    Integer idEntrada,
    String paisDocDestinatario,
    String tipoDocDestinatario,
    String numeroDocDestinatario
) {}
