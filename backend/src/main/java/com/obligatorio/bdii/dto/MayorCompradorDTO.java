package com.obligatorio.bdii.dto;

import java.math.BigDecimal;

public record MayorCompradorDTO(
    String correo,
    String paisDoc,
    String tipoDoc,
    String numeroDoc,
    Integer totalEntradas,
    BigDecimal totalGastado
) {}
