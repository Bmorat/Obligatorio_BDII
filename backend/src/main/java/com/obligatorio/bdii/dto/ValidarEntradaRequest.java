package com.obligatorio.bdii.dto;

public record ValidarEntradaRequest(
    String IdQR,
    String paisDocFuncionario,
    String tipoDocFuncionario,
    String numeroDocFuncionario
) {}
