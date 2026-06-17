package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "CodigoPostal")
public class CodigoPostal {

    @Id
    @Column(name = "Codigo")
    private String codigo;

    @Column(name = "Localidad")
    private String localidad;

    @Column(name = "Pais")
    private String pais;

    public CodigoPostal() {}
}
