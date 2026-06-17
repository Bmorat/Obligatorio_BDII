package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter @Setter @ToString
public class UsuarioGeneral extends Usuario {

    private LocalDate fechaRegistro;
    private String estadoVerificacion;

    public UsuarioGeneral() {}
}
