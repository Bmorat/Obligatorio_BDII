package com.obligatorio.bdii.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.JoinColumn;
import java.io.Serializable;

@Getter @Setter @ToString
@Entity
@IdClass(Usuario.UsuarioId.class)
public class Usuario {
    @Id
    private String pais;
    @Id
    private String tipo;
    @Id
    private String numeroDoc;
    private String correo;
    private String dirPais;
    private String dirCalle;
    private String dirNumero;
   //@ManyToOne
   // @JoinColumn(name = "codigoPostal")
    //private CodigoPostal codigoPostal;

    public Usuario() {
    }
    @Data
    public static class UsuarioId implements Serializable {
        private String pais;
        private String tipo;
        private String numeroDoc;

        public UsuarioId() {}

        public UsuarioId(String pais, String tipo, String numeroDoc) {
            this.pais = pais;
            this.tipo = tipo;
            this.numeroDoc = numeroDoc;
        }

           }
}
