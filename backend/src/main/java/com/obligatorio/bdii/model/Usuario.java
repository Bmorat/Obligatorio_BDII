package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Usuario {

    private String paisDoc;
    private String tipoDoc;
    private String numeroDoc;
    private String correo;
    private String dirCalle;
    private String dirNumero;
    private String codigoPostal;

    public Usuario() {}
}
