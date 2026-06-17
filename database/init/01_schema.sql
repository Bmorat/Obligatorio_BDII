CREATE TABLE Estadio (
    Id       INT          PRIMARY KEY AUTO_INCREMENT,
    Nombre   VARCHAR(150) NOT NULL,
    Ubicacion VARCHAR(200)
);

CREATE TABLE Sector (
    Tipo       VARCHAR(50)  NOT NULL,
    Capacidad  INT,
    IdEstadio  INT          NOT NULL,
    PRIMARY KEY (Tipo, IdEstadio),
    FOREIGN KEY (IdEstadio) REFERENCES Estadio(Id)
);

CREATE TABLE Evento (
    Id INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE Se_juega (
    IdEvento  INT  NOT NULL,
    IdEstadio INT  NOT NULL,
    Hora      TIME NOT NULL,
    Fecha     DATE NOT NULL,
    PRIMARY KEY (IdEvento, IdEstadio),
    FOREIGN KEY (IdEvento)  REFERENCES Evento(Id),
    FOREIGN KEY (IdEstadio) REFERENCES Estadio(Id)
);

CREATE TABLE Se_habilita (
    IdEvento         INT         NOT NULL,
    TipoSector       VARCHAR(50) NOT NULL,
    IdEstadio        INT         NOT NULL,
    Precio           DECIMAL(10,2),
    CapacidadHabilitada INT,
    PRIMARY KEY (IdEvento, TipoSector, IdEstadio),
    FOREIGN KEY (IdEvento)              REFERENCES Evento(Id),
    FOREIGN KEY (TipoSector, IdEstadio) REFERENCES Sector(Tipo, IdEstadio)
);

CREATE TABLE Equipo (
    Id             INT          PRIMARY KEY AUTO_INCREMENT,
    NombreDeEquipo VARCHAR(150) NOT NULL
);

CREATE TABLE Juega (
    IdEvento INT         NOT NULL,
    IdEquipo INT         NOT NULL,
    Rol      VARCHAR(50),
    PRIMARY KEY (IdEvento, IdEquipo),
    FOREIGN KEY (IdEvento) REFERENCES Evento(Id),
    FOREIGN KEY (IdEquipo) REFERENCES Equipo(Id)
);

CREATE TABLE CodigoPostal (
    CodigoPostal VARCHAR(20)  PRIMARY KEY,
    Localidad    VARCHAR(100) NOT NULL,
    Pais         VARCHAR(100) NOT NULL
);

CREATE TABLE Usuario (
    Pais        VARCHAR(100) NOT NULL,
    Tipo        VARCHAR(50)  NOT NULL,
    NumeroDoc   VARCHAR(50)  NOT NULL,
    Correo      VARCHAR(150),
    Dir_Pais    VARCHAR(100),
    Dir_Calle   VARCHAR(150),
    Dir_Numero  VARCHAR(20),
    CodigoPostal VARCHAR(20),
    PRIMARY KEY (Pais, Tipo, NumeroDoc),
    FOREIGN KEY (CodigoPostal) REFERENCES CodigoPostal(CodigoPostal)
);

CREATE TABLE Telefono (
    Pais      VARCHAR(100) NOT NULL,
    Tipo      VARCHAR(50)  NOT NULL,
    NumeroDoc VARCHAR(50)  NOT NULL,
    Telefono  VARCHAR(30)  NOT NULL,
    PRIMARY KEY (Pais, Tipo, NumeroDoc, Telefono),
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Usuario_General (
    Pais                VARCHAR(100) NOT NULL,
    Tipo                VARCHAR(50)  NOT NULL,
    NumeroDoc           VARCHAR(50)  NOT NULL,
    FechaRegistro       DATE,
    EstadoVerificacion  VARCHAR(50),
    PRIMARY KEY (Pais, Tipo, NumeroDoc),
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Administrador_de_Sede (
    Pais            VARCHAR(100) NOT NULL,
    Tipo            VARCHAR(50)  NOT NULL,
    NumeroDoc       VARCHAR(50)  NOT NULL,
    FechaAsignacion DATE,
    IdEstadio       INT          NOT NULL,
    PRIMARY KEY (Pais, Tipo, NumeroDoc),
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario(Pais, Tipo, NumeroDoc),
    FOREIGN KEY (IdEstadio)             REFERENCES Estadio(Id)
);

CREATE TABLE Funcionario_de_Validacion (
    Pais            VARCHAR(100) NOT NULL,
    Tipo            VARCHAR(50)  NOT NULL,
    NumeroDoc       VARCHAR(50)  NOT NULL,
    NumeroLegajo    VARCHAR(50),
    FechaAsignacion DATE,
    PRIMARY KEY (Pais, Tipo, NumeroDoc),
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Comision (
    IdComision INT          PRIMARY KEY AUTO_INCREMENT,
    Fecha      DATE,
    Estado     VARCHAR(50),
    Porcentaje DECIMAL(5,2),
    Pais       VARCHAR(100) NOT NULL,
    Tipo       VARCHAR(50)  NOT NULL,
    NumeroDoc  VARCHAR(50)  NOT NULL,
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Dispositivo_de_Validacion (
    Id        INT          PRIMARY KEY AUTO_INCREMENT,
    Pais      VARCHAR(100) NOT NULL,
    Tipo      VARCHAR(50)  NOT NULL,
    NumeroDoc VARCHAR(50)  NOT NULL,
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Funcionario_de_Validacion(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Compra (
    Id         INT          PRIMARY KEY AUTO_INCREMENT,
    Estado     VARCHAR(50),
    Fecha      DATE,
    MontoTotal DECIMAL(10,2),
    Pais       VARCHAR(100) NOT NULL,
    Tipo       VARCHAR(50)  NOT NULL,
    NumeroDoc  VARCHAR(50)  NOT NULL,
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario_General(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Entrada (
    IDEntrada              INT PRIMARY KEY AUTO_INCREMENT,
    Estado                 VARCHAR(50),
    NumeroVecesTransferida INT DEFAULT 0,
    PrecioTotal            DECIMAL(10,2),
    IdCompra               INT NOT NULL,
    FOREIGN KEY (IdCompra) REFERENCES Compra(Id)
);

CREATE TABLE Es_transferida (
    IDEntrada INT          NOT NULL,
    Pais      VARCHAR(100) NOT NULL,
    Tipo      VARCHAR(50)  NOT NULL,
    NumeroDoc VARCHAR(50)  NOT NULL,
    Fecha     DATE         NOT NULL,
    Estado    VARCHAR(50),
    PRIMARY KEY (IDEntrada, Pais, Tipo, NumeroDoc, Fecha),
    FOREIGN KEY (IDEntrada)             REFERENCES Entrada(IDEntrada),
    FOREIGN KEY (Pais, Tipo, NumeroDoc) REFERENCES Usuario_General(Pais, Tipo, NumeroDoc)
);

CREATE TABLE Valida (
    IDEntrada     INT NOT NULL,
    IdDispositivo INT NOT NULL,
    Ingreso       VARCHAR(50),
    CodigoAceptado VARCHAR(100),
    PRIMARY KEY (IDEntrada, IdDispositivo),
    FOREIGN KEY (IDEntrada)     REFERENCES Entrada(IDEntrada),
    FOREIGN KEY (IdDispositivo) REFERENCES Dispositivo_de_Validacion(Id)
);
