-- Estadio
INSERT IGNORE INTO Estadio (Id, Nombre, Ubicacion) VALUES
(1, 'Estadio Centenario', 'Montevideo'),
(2, 'Estadio Campeón del Siglo', 'Montevideo'),
(3, 'Gran Parque Central', 'Montevideo'),
(4, 'Estadio Luis Tróccoli', 'Montevideo'),
(5, 'Jardines del Hipódromo', 'Montevideo');

-- Sector
INSERT IGNORE INTO Sector (Tipo, Capacidad, IdEstadio) VALUES
('Tribuna', 15000, 1), ('Platea', 8000, 1), ('Palco', 500, 1),
('Tribuna', 12000, 2), ('Platea', 6000, 2), ('Palco', 400, 2),
('Tribuna', 8000, 3), ('Platea', 4000, 3), ('Palco', 300, 3),
('Tribuna', 5000, 4), ('Platea', 2500, 4), ('Palco', 200, 4),
('Tribuna', 7000, 5), ('Platea', 3500, 5), ('Palco', 250, 5);

-- Equipo
INSERT IGNORE INTO Equipo (Id, NombreDeEquipo) VALUES
(1, 'Nacional'), (2, 'Peñarol'), (3, 'Defensor Sporting'), (4, 'Danubio'), (5, 'Montevideo Wanderers');

-- Evento
INSERT IGNORE INTO Evento (Id) VALUES (1), (2), (3), (4), (5);

-- Juega
INSERT IGNORE INTO Juega (IdEvento, IdEquipo, Rol) VALUES
(1, 1, 'Local'), (1, 2, 'Visitante'), (2, 3, 'Local'), (2, 4, 'Visitante'),
(3, 6, 'Local'), (3, 5, 'Visitante'), (4, 7, 'Local'), (4, 8, 'Visitante'),
(5, 1, 'Local'), (5, 3, 'Visitante');

-- CodigoPostal
INSERT IGNORE INTO CodigoPostal (CodigoPostal, Localidad, Pais) VALUES
('11000', 'Montevideo', 'Uruguay'), ('11200', 'Pocitos', 'Uruguay'),
('11400', 'Carrasco', 'Uruguay'), ('11500', 'Punta Carretas', 'Uruguay'),
('11600', 'Parque Batlle', 'Uruguay'), ('15000', 'Canelones', 'Uruguay');

-- Usuario
INSERT IGNORE INTO Usuario (Pais, Tipo, NumeroDoc, Correo, Dir_Pais, Dir_Calle, Dir_Numero, CodigoPostal) VALUES
('Uruguay', 'CI', '12345678', 'juan.perez@email.com', 'Uruguay', '18 de Julio', '1234', '11000'),
('Uruguay', 'CI', '23456789', 'maria.garcia@email.com', 'Uruguay', 'Bvar. España', '567', '11200'),
('Uruguay', 'CI', '34567890', 'carlos.lopez@email.com', 'Uruguay', 'Av. Italia', '890', '11400'),
('Uruguay', 'CI', '45678901', 'ana.martinez@email.com', 'Uruguay', 'Av. Sarmiento', '345', '11500'),
('Uruguay', 'CI', '56789012', 'pedro.rodriguez@email.com', 'Uruguay', 'Av. Rivera', '678', '11600'),
('Uruguay', 'CI', '67890123', 'lucia.fernandez@email.com', 'Uruguay', 'Treinta y Tres', '901', '15000'),
('Uruguay', 'CI', '78901234', 'miguel.silva@email.com', 'Uruguay', 'Av. 8 de Octubre', '234', '11000'),
('Uruguay', 'CI', '89012345', 'sofia.ramos@email.com', 'Uruguay', 'Av. Brasil', '567', '11200'),
('Uruguay', 'CI', '90123456', 'diego.torres@email.com', 'Uruguay', 'Rambla', '890', '11400'),
('Uruguay', 'CI', '01234567', 'valentina.herrera@email.com', 'Uruguay', 'Colonia', '123', '11500');

-- Telefono
INSERT IGNORE INTO Telefono (Pais, Tipo, NumeroDoc, Telefono) VALUES
('Uruguay', 'CI', '12345678', '+598 91 234 567'), ('Uruguay', 'CI', '23456789', '+598 92 345 678'),
('Uruguay', 'CI', '34567890', '+598 93 456 789'), ('Uruguay', 'CI', '45678901', '+598 94 567 890'),
('Uruguay', 'CI', '56789012', '+598 95 678 901'), ('Uruguay', 'CI', '67890123', '+598 96 789 012'),
('Uruguay', 'CI', '78901234', '+598 97 890 123'), ('Uruguay', 'CI', '89012345', '+598 98 901 234'),
('Uruguay', 'CI', '90123456', '+598 99 012 345'), ('Uruguay', 'CI', '01234567', '+598 91 123 456');

-- Usuario_General
INSERT IGNORE INTO Usuario_General (Pais, Tipo, NumeroDoc, FechaRegistro, EstadoVerificacion) VALUES
('Uruguay', 'CI', '12345678', '2024-01-15', 'Verificado'),
('Uruguay', 'CI', '34567890', '2024-02-20', 'Verificado'),
('Uruguay', 'CI', '56789012', '2024-03-10', 'Pendiente'),
('Uruguay', 'CI', '78901234', '2024-04-05', 'Verificado');

-- Administrador_de_Sede
INSERT IGNORE INTO Administrador_de_Sede (Pais, Tipo, NumeroDoc, FechaAsignacion, IdEstadio) VALUES
('Uruguay', 'CI', '23456789', '2024-01-01', 1),
('Uruguay', 'CI', '45678901', '2024-01-15', 2),
('Uruguay', 'CI', '67890123', '2024-02-01', 3);

-- Funcionario_de_Validacion
INSERT IGNORE INTO Funcionario_de_Validacion (Pais, Tipo, NumeroDoc, NumeroLegajo, FechaAsignacion) VALUES
('Uruguay', 'CI', '89012345', 'LEG-001', '2024-01-10'),
('Uruguay', 'CI', '90123456', 'LEG-002', '2024-01-20'),
('Uruguay', 'CI', '01234567', 'LEG-003', '2024-02-15');

-- Dispositivo_de_Validacion
INSERT IGNORE INTO Dispositivo_de_Validacion (Id, Pais, Tipo, NumeroDoc) VALUES
(1, 'Uruguay', 'CI', '89012345'), (2, 'Uruguay', 'CI', '89012345'),
(3, 'Uruguay', 'CI', '90123456'), (4, 'Uruguay', 'CI', '01234567');

-- Comision
INSERT IGNORE INTO Comision (IdComision, Fecha, Estado, Porcentaje, Pais, Tipo, NumeroDoc) VALUES
(1, '2024-06-01', 'Pendiente', 5.00, 'Uruguay', 'CI', '89012345'),
(2, '2024-06-15', 'Pagada', 3.50, 'Uruguay', 'CI', '90123456'),
(3, '2024-07-01', 'Pendiente', 4.00, 'Uruguay', 'CI', '01234567'),
(4, '2024-07-15', 'Pagada', 5.00, 'Uruguay', 'CI', '89012345');

-- Compra
INSERT IGNORE INTO Compra (Id, Estado, Fecha, MontoTotal, Pais, Tipo, NumeroDoc) VALUES
(1, 'Completada', '2024-05-10', 1500.00, 'Uruguay', 'CI', '12345678'),
(2, 'Completada', '2024-05-12', 2400.00, 'Uruguay', 'CI', '34567890'),
(3, 'Pendiente', '2024-05-20', 800.00, 'Uruguay', 'CI', '12345678'),
(4, 'Completada', '2024-06-01', 3200.00, 'Uruguay', 'CI', '56789012'),
(5, 'Cancelada', '2024-06-15', 600.00, 'Uruguay', 'CI', '78901234'),
(6, 'Completada', '2024-06-20', 5000.00, 'Uruguay', 'CI', '34567890');

-- Entrada
INSERT IGNORE INTO Entrada (IDEntrada, Estado, NumeroVecesTransferida, PrecioTotal, IdCompra) VALUES
(1, 'Activa', 0, 500.00, 1), (2, 'Activa', 0, 500.00, 1),
(3, 'Transferida', 1, 500.00, 1), (4, 'Activa', 0, 1200.00, 2),
(5, 'Usada', 0, 1200.00, 2), (6, 'Activa', 0, 400.00, 3),
(7, 'Cancelada', 0, 400.00, 3), (8, 'Activa', 0, 800.00, 4),
(9, 'Activa', 0, 800.00, 4), (10, 'Usada', 0, 800.00, 4),
(11, 'Usada', 0, 800.00, 4), (12, 'Cancelada', 0, 300.00, 5),
(13, 'Cancelada', 0, 300.00, 5), (14, 'Activa', 0, 2500.00, 6),
(15, 'Activa', 0, 2500.00, 6);

-- Es_transferida
INSERT IGNORE INTO Es_transferida (IDEntrada, Pais, Tipo, NumeroDoc, Fecha, Estado) VALUES
(3, 'Uruguay', 'CI', '34567890', '2024-05-15', 'Aceptada'),
(3, 'Uruguay', 'CI', '56789012', '2024-05-18', 'Pendiente'),
(1, 'Uruguay', 'CI', '78901234', '2024-05-25', 'Aceptada'),
(9, 'Uruguay', 'CI', '12345678', '2024-06-05', 'Pendiente');

-- Valida
INSERT IGNORE INTO Valida (IDEntrada, IdDispositivo, Ingreso, CodigoAceptado) VALUES
(5, 1, 'Permitido', 'ABC123'), (10, 2, 'Permitido', 'DEF456'),
(11, 3, 'Permitido', 'GHI789'), (1, 1, 'Denegado', NULL),
(5, 4, 'Permitido', 'ABC123'), (14, 2, 'Permitido', 'JKL012');
