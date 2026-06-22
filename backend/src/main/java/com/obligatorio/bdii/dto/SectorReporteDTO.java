package com.obligatorio.bdii.dto;

import java.math.BigDecimal;

public record SectorReporteDTO(
    String tipo,
    Integer cantidadVendida,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {}