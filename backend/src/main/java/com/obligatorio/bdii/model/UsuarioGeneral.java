package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter @Setter @ToString
@Entity
@Table(name = "Usuario_General")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "PaisDoc", referencedColumnName = "PaisDoc"),
    @PrimaryKeyJoinColumn(name = "TipoDoc", referencedColumnName = "TipoDoc"),
    @PrimaryKeyJoinColumn(name = "NumeroDoc", referencedColumnName = "NumeroDoc")
})
public class UsuarioGeneral extends Usuario {

    @Column(name = "FechaRegistro")
    private LocalDate fechaRegistro;

    @Column(name = "EstadoVerificacion")
    private String estadoVerificacion;

    public UsuarioGeneral() {}
}
