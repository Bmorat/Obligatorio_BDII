package com.obligatorio.bdii.dto;

public record DatabaseStatusResponse(
    boolean connected,
    String database,
    int tables
) {
}
