package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter @Setter @ToString
public class AdministradorSede extends Usuario {

    private LocalDate fechaAsignacion;

    public AdministradorSede() {}
}
