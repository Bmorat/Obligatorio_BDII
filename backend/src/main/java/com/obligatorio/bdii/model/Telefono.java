package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Getter @Setter @ToString
@Entity
@Table(name = "Telefono")
@IdClass(Telefono.TelefonoId.class)
public class Telefono {

    @Id
    @Column(name = "PaisDoc")
    private String paisDoc;

    @Id
    @Column(name = "TipoDoc")
    private String tipoDoc;

    @Id
    @Column(name = "NumeroDoc")
    private String numeroDoc;

    @Id
    @Column(name = "NumTelefono")
    private String numTelefono;

    public Telefono() {}

    @Getter @Setter
    @ToString
    public static class TelefonoId implements Serializable {
        private String paisDoc;
        private String tipoDoc;
        private String numeroDoc;
        private String numTelefono;

        public TelefonoId() {}

        public TelefonoId(String paisDoc, String tipoDoc, String numeroDoc, String numTelefono) {
            this.paisDoc = paisDoc;
            this.tipoDoc = tipoDoc;
            this.numeroDoc = numeroDoc;
            this.numTelefono = numTelefono;
        }
    }
}
