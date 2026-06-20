package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CredencialUsuario {

    private String paisDoc;
    private String tipoDoc;
    private String numeroDoc;
    private String passwordHash;

    public CredencialUsuario() {}
}
