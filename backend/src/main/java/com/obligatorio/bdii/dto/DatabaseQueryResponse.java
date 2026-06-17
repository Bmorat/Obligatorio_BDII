package com.obligatorio.bdii.dto;

import java.util.List;
import java.util.Map;

public record DatabaseQueryResponse(
    List<String> columns,
    List<Map<String, Object>> rows,
    int rowCount
) {
}
