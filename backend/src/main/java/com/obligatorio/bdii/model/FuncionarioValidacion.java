package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FuncionarioValidacion extends Usuario {

    private String numeroLegajo;

    public FuncionarioValidacion() {}
}
