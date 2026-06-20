package com.obligatorio.bdii.dto;

public record LoginRequest(
    String correo,
    String password
) {
}
