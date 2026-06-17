package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "Funcionario_Validacion")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "PaisDoc", referencedColumnName = "PaisDoc"),
    @PrimaryKeyJoinColumn(name = "TipoDoc", referencedColumnName = "TipoDoc"),
    @PrimaryKeyJoinColumn(name = "NumeroDoc", referencedColumnName = "NumeroDoc")
})
public class FuncionarioValidacion extends Usuario {

    @Column(name = "NumeroLegajo")
    private String numeroLegajo;

    public FuncionarioValidacion() {}
}
