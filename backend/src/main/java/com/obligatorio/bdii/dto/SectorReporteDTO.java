package com.obligatorio.bdii.dto;

import java.math.BigDecimal;
import com.obligatorio.bdii.model.SectorTipo;

public record SectorReporteDTO(
    SectorTipo tipo,
    Integer cantidadVendida,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {}