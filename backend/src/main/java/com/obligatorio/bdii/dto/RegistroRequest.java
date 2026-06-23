package com.obligatorio.bdii.dto;

import java.util.List;

public record RegistroRequest(
    String correo,
    String paisDoc,
    String tipoDoc,
    String numeroDoc,
    String dirCalle,
    String dirNumero,
    String password,
    String codigoPostal,
    String localidad,
    String paisCP,
    List<String> telefonos
) {}
