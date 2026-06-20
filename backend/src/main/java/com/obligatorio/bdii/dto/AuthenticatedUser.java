package com.obligatorio.bdii.dto;

public record AuthenticatedUser(
    String correo,
    String paisDoc,
    String tipoDoc,
    String numeroDoc,
    String passwordHash,
    String rol
) 
{}