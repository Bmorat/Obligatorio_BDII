package com.obligatorio.bdii.dto;

public record LoginResponse(
    String token,
    String correo,
    String paisDoc,
    String tipoDoc,
    String numeroDoc,
    String rol
) {
}
