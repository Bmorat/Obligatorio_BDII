CREATE TABLE Pais (
    nombre VARCHAR(100) PRIMARY KEY
);

CREATE TABLE Persona (
    id_persona    INT PRIMARY KEY AUTO_INCREMENT,
    usuario       VARCHAR(100) UNIQUE NOT NULL,
    correo        VARCHAR(150) NOT NULL,
    pais          VARCHAR(100) NOT NULL,
    tipo_doc      VARCHAR(50)  NOT NULL,
    numero_doc    VARCHAR(50)  NOT NULL,
    FOREIGN KEY (pais) REFERENCES Pais(nombre)
);

CREATE TABLE CodigoPostal (
    codigo_postal VARCHAR(20)  PRIMARY KEY,
    localidad     VARCHAR(100) NOT NULL,
    pais          VARCHAR(100) NOT NULL,
    FOREIGN KEY (pais) REFERENCES Pais(nombre)
);

CREATE TABLE Direccion (
    id_persona    INT PRIMARY KEY,
    codigo_postal VARCHAR(20),
    calle         VARCHAR(150),
    numero        VARCHAR(20),
    FOREIGN KEY (id_persona)    REFERENCES Persona(id_persona),
    FOREIGN KEY (codigo_postal) REFERENCES CodigoPostal(codigo_postal)
);

CREATE TABLE Telefono (
    id_persona INT          NOT NULL,
    telefono   VARCHAR(30)  NOT NULL,
    PRIMARY KEY (id_persona, telefono),
    FOREIGN KEY (id_persona) REFERENCES Persona(id_persona)
);

CREATE TABLE Usuario_General (
    id_persona     INT PRIMARY KEY,
    fecha_registro DATE NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES Persona(id_persona)
);

CREATE TABLE Funcionario_Validacion (
    id_persona         INT PRIMARY KEY,
    numero_legajo      VARCHAR(50)  NOT NULL,
    estado_verificacion VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES Persona(id_persona)
);

CREATE TABLE Administrador_Sede (
    id_persona        INT  PRIMARY KEY,
    fecha_asignacion  DATE NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES Persona(id_persona)
);

CREATE TABLE Estadio (
    id_estadio INT PRIMARY KEY AUTO_INCREMENT,
    nombre     VARCHAR(150) NOT NULL,
    ubicacion  VARCHAR(200)
);

CREATE TABLE Sector (
    id_sector  INT PRIMARY KEY AUTO_INCREMENT,
    id_estadio INT          NOT NULL,
    tipo       VARCHAR(50),
    capacidad  INT,
    FOREIGN KEY (id_estadio) REFERENCES Estadio(id_estadio)
);

CREATE TABLE Equipo (
    id_equipo        INT PRIMARY KEY AUTO_INCREMENT,
    nombre_de_equipo VARCHAR(150) NOT NULL
);

CREATE TABLE Evento (
    id_evento  INT PRIMARY KEY AUTO_INCREMENT,
    id_estadio INT  NOT NULL,
    fecha      DATE NOT NULL,
    hora       TIME NOT NULL,
    FOREIGN KEY (id_estadio) REFERENCES Estadio(id_estadio)
);

CREATE TABLE Juega (
    id_evento INT NOT NULL,
    id_equipo INT NOT NULL,
    rol       VARCHAR(50),
    PRIMARY KEY (id_evento, id_equipo),
    FOREIGN KEY (id_evento) REFERENCES Evento(id_evento),
    FOREIGN KEY (id_equipo) REFERENCES Equipo(id_equipo)
);

CREATE TABLE Gestiona (
    id_persona INT  NOT NULL,
    id_estadio INT  NOT NULL,
    fecha      DATE NOT NULL,
    PRIMARY KEY (id_persona, id_estadio),
    FOREIGN KEY (id_persona) REFERENCES Administrador_Sede(id_persona),
    FOREIGN KEY (id_estadio) REFERENCES Estadio(id_estadio)
);

CREATE TABLE Se_Habilita (
    id_evento           INT NOT NULL,
    id_sector           INT NOT NULL,
    precio              DECIMAL(10,2),
    capacidad_habilitada INT,
    precio_total        DECIMAL(10,2),
    PRIMARY KEY (id_evento, id_sector),
    FOREIGN KEY (id_evento) REFERENCES Evento(id_evento),
    FOREIGN KEY (id_sector) REFERENCES Sector(id_sector)
);

CREATE TABLE Compra (
    id_compra   INT PRIMARY KEY AUTO_INCREMENT,
    id_persona  INT            NOT NULL,
    estado      VARCHAR(50),
    monto_total DECIMAL(10,2),
    FOREIGN KEY (id_persona) REFERENCES Usuario_General(id_persona)
);

CREATE TABLE Entrada (
    id_entrada                INT PRIMARY KEY AUTO_INCREMENT,
    id_compra                 INT NOT NULL,
    id_evento                 INT NOT NULL,
    id_sector                 INT NOT NULL,
    estado                    VARCHAR(50),
    numero_veces_transferida  INT DEFAULT 0,
    FOREIGN KEY (id_compra)            REFERENCES Compra(id_compra),
    FOREIGN KEY (id_evento, id_sector) REFERENCES Se_Habilita(id_evento, id_sector)
);

CREATE TABLE QR (
    id_qr      INT PRIMARY KEY AUTO_INCREMENT,
    id_entrada INT UNIQUE NOT NULL,
    FOREIGN KEY (id_entrada) REFERENCES Entrada(id_entrada)
);

CREATE TABLE Dispositivo_Validacion (
    id_dispositivo  INT PRIMARY KEY AUTO_INCREMENT,
    id_funcionario  INT NOT NULL,
    codigo_aceptado VARCHAR(100),
    estado          VARCHAR(50),
    FOREIGN KEY (id_funcionario) REFERENCES Funcionario_Validacion(id_persona)
);

CREATE TABLE Ingreso (
    id_qr           INT NOT NULL,
    id_dispositivo  INT NOT NULL,
    estado          VARCHAR(50),
    PRIMARY KEY (id_qr, id_dispositivo),
    FOREIGN KEY (id_qr)          REFERENCES QR(id_qr),
    FOREIGN KEY (id_dispositivo) REFERENCES Dispositivo_Validacion(id_dispositivo)
);

CREATE TABLE Transferencia (
    id_entrada     INT  NOT NULL,
    id_persona_origen INT NOT NULL,
    id_persona_dest   INT NOT NULL,
    fecha          DATE NOT NULL,
    PRIMARY KEY (id_entrada, id_persona_origen, id_persona_dest, fecha),
    FOREIGN KEY (id_entrada)      REFERENCES Entrada(id_entrada),
    FOREIGN KEY (id_persona_origen) REFERENCES Usuario_General(id_persona),
    FOREIGN KEY (id_persona_dest)   REFERENCES Usuario_General(id_persona)
);

CREATE TABLE Comision (
    id_comision INT PRIMARY KEY AUTO_INCREMENT,
    id_compra   INT            NOT NULL,
    fecha       DATE,
    estado      VARCHAR(50),
    porcentaje  DECIMAL(5,2),
    FOREIGN KEY (id_compra) REFERENCES Compra(id_compra)
);