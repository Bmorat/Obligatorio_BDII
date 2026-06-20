CREATE TABLE Estadio (
    Id          INT          NOT NULL AUTO_INCREMENT,
    Nombre      VARCHAR(100) NOT NULL,
    Ubicacion   VARCHAR(200) NOT NULL,
    CONSTRAINT PK_Estadio PRIMARY KEY (Id)
);

CREATE TABLE Sector (
    IdEstadio   INT         NOT NULL,
    Tipo        VARCHAR(50) NOT NULL,
    Capacidad   INT         NOT NULL,
    CONSTRAINT PK_Sector PRIMARY KEY (IdEstadio, Tipo),
    CONSTRAINT FK_Sector_Estadio FOREIGN KEY (IdEstadio) REFERENCES Estadio(Id)
);

CREATE TABLE CodigoPostal (
    Codigo    VARCHAR(10)  NOT NULL,
    Localidad VARCHAR(100) NOT NULL,
    Pais      VARCHAR(100) NOT NULL,
    CONSTRAINT PK_CodigoPostal PRIMARY KEY (Codigo)
);

CREATE TABLE Usuario (
    PaisDoc      CHAR(3)      NOT NULL,
    TipoDoc      VARCHAR(20)  NOT NULL,
    NumeroDoc    VARCHAR(30)  NOT NULL,
    Correo       VARCHAR(150) NOT NULL,
    Dir_Calle    VARCHAR(150) NOT NULL,
    Dir_Numero   VARCHAR(10)  NOT NULL,
    CodigoPostal VARCHAR(10)  NOT NULL,
    CONSTRAINT PK_Usuario PRIMARY KEY (PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT AK_Usuario_Correo UNIQUE (Correo),
    CONSTRAINT FK_Usuario_CodigoPostal FOREIGN KEY (CodigoPostal) REFERENCES CodigoPostal(Codigo)
);

CREATE TABLE CredencialUsuario (
    PaisDoc      CHAR(3)      NOT NULL,
    TipoDoc      VARCHAR(20)  NOT NULL,
    NumeroDoc    VARCHAR(30)  NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CredencialUsuario PRIMARY KEY (PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT FK_CredencialUsuario_Usuario FOREIGN KEY (PaisDoc, TipoDoc, NumeroDoc) REFERENCES Usuario(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Telefono (
    PaisDoc     CHAR(3)     NOT NULL,
    TipoDoc     VARCHAR(20) NOT NULL,
    NumeroDoc   VARCHAR(30) NOT NULL,
    NumTelefono VARCHAR(20) NOT NULL,
    CONSTRAINT PK_Telefono PRIMARY KEY (PaisDoc, TipoDoc, NumeroDoc, NumTelefono),
    CONSTRAINT FK_Telefono_Usuario FOREIGN KEY (PaisDoc, TipoDoc, NumeroDoc) REFERENCES Usuario(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Administrador_Sede (
    PaisDoc         CHAR(3)     NOT NULL,
    TipoDoc         VARCHAR(20) NOT NULL,
    NumeroDoc       VARCHAR(30) NOT NULL,
    FechaAsignacion DATE        NOT NULL,
    CONSTRAINT PK_AdminSede PRIMARY KEY (PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT FK_AdminSede_Usuario FOREIGN KEY (PaisDoc, TipoDoc, NumeroDoc) REFERENCES Usuario(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Usuario_General (
    PaisDoc            CHAR(3)     NOT NULL,
    TipoDoc            VARCHAR(20) NOT NULL,
    NumeroDoc          VARCHAR(30) NOT NULL,
    FechaRegistro      DATE        NOT NULL,
    EstadoVerificacion VARCHAR(30) NOT NULL,
    CONSTRAINT PK_UsuarioGeneral PRIMARY KEY (PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT FK_UsuarioGeneral_Usuario FOREIGN KEY (PaisDoc, TipoDoc, NumeroDoc) REFERENCES Usuario(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Funcionario_Validacion (
    PaisDoc      CHAR(3)     NOT NULL,
    TipoDoc      VARCHAR(20) NOT NULL,
    NumeroDoc    VARCHAR(30) NOT NULL,
    NumeroLegajo VARCHAR(30) NOT NULL,
    CONSTRAINT PK_FuncValidacion PRIMARY KEY (PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT FK_FuncValidacion_Usuario FOREIGN KEY (PaisDoc, TipoDoc, NumeroDoc) REFERENCES Usuario(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Dispositivo_Validacion (
    IdDispositivo INT         NOT NULL AUTO_INCREMENT,
    PaisDocFunc   CHAR(3)     NOT NULL,
    TipoDocFunc   VARCHAR(20) NOT NULL,
    NumeroDocFunc VARCHAR(30) NOT NULL,
    CONSTRAINT PK_Dispositivo PRIMARY KEY (IdDispositivo),
    CONSTRAINT FK_Dispositivo_Funcionario FOREIGN KEY (PaisDocFunc, TipoDocFunc, NumeroDocFunc) REFERENCES Funcionario_Validacion(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Evento (
    Id             INT         NOT NULL AUTO_INCREMENT,
    Fecha          DATE        NOT NULL,
    Hora           TIME        NOT NULL,
    IdEstadio      INT         NOT NULL,
    PaisDocAdmin   CHAR(3)     NOT NULL,
    TipoDocAdmin   VARCHAR(20) NOT NULL,
    NumeroDocAdmin VARCHAR(30) NOT NULL,
    CONSTRAINT PK_Evento PRIMARY KEY (Id),
    CONSTRAINT FK_Evento_Estadio FOREIGN KEY (IdEstadio) REFERENCES Estadio(Id),
    CONSTRAINT FK_Evento_AdminSede FOREIGN KEY (PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin) REFERENCES Administrador_Sede(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Equipo (
    Id             INT          NOT NULL AUTO_INCREMENT,
    NombreDeEquipo VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Equipo PRIMARY KEY (Id)
);

CREATE TABLE Juega (
    IdEvento INT         NOT NULL,
    IdEquipo INT         NOT NULL,
    Rol      VARCHAR(10) NOT NULL,
    CONSTRAINT PK_Juega PRIMARY KEY (IdEvento, IdEquipo),
    CONSTRAINT FK_Juega_Evento FOREIGN KEY (IdEvento) REFERENCES Evento(Id),
    CONSTRAINT FK_Juega_Equipo FOREIGN KEY (IdEquipo) REFERENCES Equipo(Id)
);

CREATE TABLE Se_habilita (
    IdEvento            INT            NOT NULL,
    IdEstadio           INT            NOT NULL,
    Tipo                VARCHAR(50)    NOT NULL,
    Precio              DECIMAL(10,2)  NOT NULL,
    CapacidadHabilitada INT            NOT NULL,
    CONSTRAINT PK_SeHabilita PRIMARY KEY (IdEvento, IdEstadio, Tipo),
    CONSTRAINT FK_SeHabilita_Evento FOREIGN KEY (IdEvento) REFERENCES Evento(Id),
    CONSTRAINT FK_SeHabilita_Sector FOREIGN KEY (IdEstadio, Tipo) REFERENCES Sector(IdEstadio, Tipo)
);

CREATE TABLE Comision (
    IdComision INT          NOT NULL AUTO_INCREMENT,
    Vigente    BOOLEAN      NOT NULL DEFAULT FALSE,
    Fecha      DATE         NOT NULL,
    Porcentaje DECIMAL(5,2) NOT NULL,
    CONSTRAINT PK_Comision PRIMARY KEY (IdComision)
);

CREATE TABLE Compra (
    Id               INT           NOT NULL AUTO_INCREMENT,
    Estado           VARCHAR(30)   NOT NULL,
    Fecha            DATE          NOT NULL,
    MontoTotal       DECIMAL(12,2) NULL,
    PaisDocUsuario   CHAR(3)       NOT NULL,
    TipoDocUsuario   VARCHAR(20)   NOT NULL,
    NumeroDocUsuario VARCHAR(30)   NOT NULL,
    IdComision       INT           NOT NULL,
    CONSTRAINT PK_Compra PRIMARY KEY (Id),
    CONSTRAINT FK_Compra_UsuarioGeneral FOREIGN KEY (PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario) REFERENCES Usuario_General(PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT FK_Compra_Comision FOREIGN KEY (IdComision) REFERENCES Comision(IdComision)
);

CREATE TABLE QR (
    IdQR VARCHAR(64) NOT NULL,
    CONSTRAINT PK_QR PRIMARY KEY (IdQR)
);

CREATE TABLE Entrada (
    IdEntrada               INT          NOT NULL AUTO_INCREMENT,
    Estado                  VARCHAR(30)  NOT NULL,
    NumeroVecesTransferida  TINYINT      NOT NULL DEFAULT 0,
    IdCompra                INT          NOT NULL,
    IdEvento                INT          NOT NULL,
    IdEstadio               INT          NOT NULL,
    Tipo                    VARCHAR(50)  NOT NULL,
    IdQR                    VARCHAR(64)  NOT NULL,
    IdDispositivoValidacion INT          NULL,
    CodigoAceptado          VARCHAR(64)  NULL,
    FechaHoraValidacion     DATETIME     NULL,
    CONSTRAINT PK_Entrada PRIMARY KEY (IdEntrada),
    CONSTRAINT AK_Entrada_QR UNIQUE (IdQR),
    CONSTRAINT FK_Entrada_Compra FOREIGN KEY (IdCompra) REFERENCES Compra(Id),
    CONSTRAINT FK_Entrada_SeHabilita FOREIGN KEY (IdEvento, IdEstadio, Tipo) REFERENCES Se_habilita(IdEvento, IdEstadio, Tipo),
    CONSTRAINT FK_Entrada_QR FOREIGN KEY (IdQR) REFERENCES QR(IdQR),
    CONSTRAINT FK_Entrada_Dispositivo FOREIGN KEY (IdDispositivoValidacion) REFERENCES Dispositivo_Validacion(IdDispositivo)
);

CREATE TABLE Transferencia (
    IdTransferencia     INT         NOT NULL AUTO_INCREMENT,
    FechaTransferencia  DATE        NOT NULL,
    EstadoTransferencia VARCHAR(30) NOT NULL,
    IdEntrada           INT         NOT NULL,
    PaisDocUsuario      CHAR(3)     NOT NULL,
    TipoDocUsuario      VARCHAR(20) NOT NULL,
    NumeroDocUsuario    VARCHAR(30) NOT NULL,
    CONSTRAINT PK_Transferencia PRIMARY KEY (IdTransferencia),
    CONSTRAINT FK_Transferencia_Entrada FOREIGN KEY (IdEntrada) REFERENCES Entrada(IdEntrada),
    CONSTRAINT FK_Transferencia_UsuarioGeneral FOREIGN KEY (PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario) REFERENCES Usuario_General(PaisDoc, TipoDoc, NumeroDoc)
);

CREATE TABLE Asignado_a (
    PaisDocFunc   CHAR(3)     NOT NULL,
    TipoDocFunc   VARCHAR(20) NOT NULL,
    NumeroDocFunc VARCHAR(30) NOT NULL,
    IdEvento      INT         NOT NULL,
    IdEstadio     INT         NOT NULL,
    Tipo          VARCHAR(50) NOT NULL,
    CONSTRAINT PK_AsignadoA PRIMARY KEY (PaisDocFunc, TipoDocFunc, NumeroDocFunc, IdEvento, IdEstadio, Tipo),
    CONSTRAINT FK_AsignadoA_Funcionario FOREIGN KEY (PaisDocFunc, TipoDocFunc, NumeroDocFunc) REFERENCES Funcionario_Validacion(PaisDoc, TipoDoc, NumeroDoc),
    CONSTRAINT FK_AsignadoA_SeHabilita FOREIGN KEY (IdEvento, IdEstadio, Tipo) REFERENCES Se_habilita(IdEvento, IdEstadio, Tipo)
);

DELIMITER //

CREATE TRIGGER TRG_UsuarioGeneral_UnicoTipo
BEFORE INSERT ON Usuario_General
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Administrador_Sede
        WHERE PaisDoc = NEW.PaisDoc
          AND TipoDoc = NEW.TipoDoc
          AND NumeroDoc = NEW.NumeroDoc
    ) OR EXISTS (
        SELECT 1
        FROM Funcionario_Validacion
        WHERE PaisDoc = NEW.PaisDoc
          AND TipoDoc = NEW.TipoDoc
          AND NumeroDoc = NEW.NumeroDoc
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario ya tiene otro tipo asignado';
    END IF;
END//

CREATE TRIGGER TRG_AdministradorSede_UnicoTipo
BEFORE INSERT ON Administrador_Sede
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Usuario_General
        WHERE PaisDoc = NEW.PaisDoc
          AND TipoDoc = NEW.TipoDoc
          AND NumeroDoc = NEW.NumeroDoc
    ) OR EXISTS (
        SELECT 1
        FROM Funcionario_Validacion
        WHERE PaisDoc = NEW.PaisDoc
          AND TipoDoc = NEW.TipoDoc
          AND NumeroDoc = NEW.NumeroDoc
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario ya tiene otro tipo asignado';
    END IF;
END//

CREATE TRIGGER TRG_FuncionarioValidacion_UnicoTipo
BEFORE INSERT ON Funcionario_Validacion
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Usuario_General
        WHERE PaisDoc = NEW.PaisDoc
          AND TipoDoc = NEW.TipoDoc
          AND NumeroDoc = NEW.NumeroDoc
    ) OR EXISTS (
        SELECT 1
        FROM Administrador_Sede
        WHERE PaisDoc = NEW.PaisDoc
          AND TipoDoc = NEW.TipoDoc
          AND NumeroDoc = NEW.NumeroDoc
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario ya tiene otro tipo asignado';
    END IF;
END//

DELIMITER ;
