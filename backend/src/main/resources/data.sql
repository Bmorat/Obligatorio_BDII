-- CodigoPostal
INSERT IGNORE INTO CodigoPostal (Codigo, Localidad, Pais) VALUES
('11000', 'Montevideo Centro', 'Uruguay'),
('11200', 'Pocitos',           'Uruguay'),
('11400', 'Carrasco',          'Uruguay'),
('11500', 'Punta Carretas',    'Uruguay'),
('11600', 'Parque Batlle',     'Uruguay'),
('15000', 'Canelones',         'Uruguay');

-- Usuario
INSERT IGNORE INTO Usuario (PaisDoc, TipoDoc, NumeroDoc, Correo, Dir_Calle, Dir_Numero, CodigoPostal) VALUES
('URY', 'CI', '12345678', 'juan.perez@email.com',        '18 de Julio',     '1234', '11000'),
('URY', 'CI', '23456789', 'maria.garcia@email.com',      'Bvar. España',    '567',  '11200'),
('URY', 'CI', '34567890', 'carlos.lopez@email.com',      'Av. Italia',      '890',  '11400'),
('URY', 'CI', '45678901', 'ana.martinez@email.com',      'Av. Sarmiento',   '345',  '11500'),
('URY', 'CI', '56789012', 'pedro.rodriguez@email.com',   'Av. Rivera',      '678',  '11600'),
('URY', 'CI', '67890123', 'lucia.fernandez@email.com',   'Treinta y Tres',  '901',  '15000'),
('URY', 'CI', '78901234', 'miguel.silva@email.com',      'Av. 8 de Octubre','234',  '11000'),
('URY', 'CI', '89012345', 'sofia.ramos@email.com',       'Av. Brasil',      '567',  '11200'),
('URY', 'CI', '90123456', 'diego.torres@email.com',      'Rambla',          '890',  '11400'),
('URY', 'CI', '01234567', 'valentina.herrera@email.com', 'Colonia',         '123',  '11500');

-- CredencialUsuario
INSERT IGNORE INTO CredencialUsuario (PaisDoc, TipoDoc, NumeroDoc, PasswordHash) VALUES
('URY', 'CI', '12345678', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '23456789', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '34567890', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '45678901', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '56789012', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '67890123', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '78901234', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '89012345', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '90123456', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy'),
('URY', 'CI', '01234567', '$2y$10$skeckeaqfHRroPaN0exaFekI4S56H1O44ICh/rv36s.ACa8u84Rxy');

-- Telefono
INSERT IGNORE INTO Telefono (PaisDoc, TipoDoc, NumeroDoc, NumTelefono) VALUES
('URY', 'CI', '12345678', '+59891234567'),
('URY', 'CI', '23456789', '+59892345678'),
('URY', 'CI', '34567890', '+59893456789'),
('URY', 'CI', '45678901', '+59894567890'),
('URY', 'CI', '56789012', '+59895678901'),
('URY', 'CI', '67890123', '+59896789012'),
('URY', 'CI', '78901234', '+59897890123'),
('URY', 'CI', '89012345', '+59898901234'),
('URY', 'CI', '90123456', '+59899012345'),
('URY', 'CI', '01234567', '+59891123456');

-- Equipo
INSERT IGNORE INTO Equipo (Id, NombreDeEquipo) VALUES
(1, 'Nacional'),
(2, 'Peñarol'),
(3, 'Defensor Sporting'),
(4, 'Danubio'),
(5, 'Montevideo Wanderers');

-- Administrador_Sede
INSERT IGNORE INTO Administrador_Sede (PaisDoc, TipoDoc, NumeroDoc, FechaAsignacion) VALUES
('URY', 'CI', '23456789', '2024-01-01'),
('URY', 'CI', '45678901', '2024-01-15'),
('URY', 'CI', '67890123', '2024-02-01');

-- Usuario_General
INSERT IGNORE INTO Usuario_General (PaisDoc, TipoDoc, NumeroDoc, FechaRegistro, EstadoVerificacion) VALUES
('URY', 'CI', '12345678', '2024-01-15', TRUE),
('URY', 'CI', '34567890', '2024-02-20', TRUE),
('URY', 'CI', '56789012', '2024-03-10', FALSE),
('URY', 'CI', '78901234', '2024-04-05', TRUE);

-- Funcionario_Validacion
INSERT IGNORE INTO Funcionario_Validacion (PaisDoc, TipoDoc, NumeroDoc, NumeroLegajo) VALUES
('URY', 'CI', '89012345', 'LEG-001'),
('URY', 'CI', '90123456', 'LEG-002'),
('URY', 'CI', '01234567', 'LEG-003');

-- Dispositivo_Validacion
INSERT IGNORE INTO Dispositivo_Validacion (IdDispositivo, PaisDocFunc, TipoDocFunc, NumeroDocFunc) VALUES
(1, 'URY', 'CI', '89012345'),
(2, 'URY', 'CI', '89012345'),
(3, 'URY', 'CI', '90123456'),
(4, 'URY', 'CI', '01234567');

-- Estadio
INSERT IGNORE INTO Estadio (Id, Nombre, Ubicacion, PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin) VALUES
(1, 'Estadio Centenario',        'Montevideo', 'URY', 'CI', '23456789'),
(2, 'Estadio Campeón del Siglo', 'Montevideo', 'URY', 'CI', '23456789'),
(3, 'Gran Parque Central',       'Montevideo', 'URY', 'CI', '67890123'),
(4, 'Estadio Luis Tróccoli',     'Montevideo', 'URY', 'CI', '45678901'),
(5, 'Jardines del Hipódromo',    'Montevideo', 'URY', 'CI', '45678901');

-- Sector
INSERT IGNORE INTO Sector (IdEstadio, TipoSector, Capacidad) VALUES
(1, 'A', 15000), (1, 'B', 8000), (1, 'C', 500),
(2, 'A', 12000), (2, 'B', 6000), (2, 'C', 400),
(3, 'A', 8000),  (3, 'B', 4000), (3, 'C', 300),
(4, 'A', 5000),  (4, 'B', 2500), (4, 'C', 200),
(5, 'A', 7000),  (5, 'B', 3500), (5, 'C', 250),
(1, 'D', 300),  (2, 'D', 250),  (3, 'D', 200),
(4, 'D', 150),  (5, 'D', 180);

-- Evento
INSERT IGNORE INTO Evento (IdEvento, Fecha, Hora, IdEstadio) VALUES
(1, '2024-06-01', '15:00:00', 1),
(2, '2024-06-08', '17:00:00', 2),
(3, '2024-06-15', '15:00:00', 1),
(4, '2024-06-22', '17:00:00', 3),
(5, '2024-06-29', '15:00:00', 2);

-- Juega
INSERT IGNORE INTO Juega (IdEvento, IdEquipo, Rol) VALUES
(1, 1, 'Local'),  (1, 2, 'Visitante'),
(2, 3, 'Local'),  (2, 4, 'Visitante'),
(3, 1, 'Local'),  (3, 3, 'Visitante'),
(4, 2, 'Local'),  (4, 5, 'Visitante'),
(5, 4, 'Local'),  (5, 1, 'Visitante');

-- Se_habilita
INSERT IGNORE INTO Se_habilita (IdEvento, IdEstadio, Tipo, Precio, CapacidadHabilitada) VALUES
(1, 1, 'A', 500.00,  10000),
(1, 1, 'B',  1200.00, 5000),
(1, 1, 'C',   2500.00, 200),
(2, 2, 'A', 600.00,  8000),
(2, 2, 'B',  1500.00, 4000),
(3, 1, 'A', 500.00,  10000),
(3, 1, 'B',  1200.00, 5000),
(4, 3, 'A', 400.00,  6000),
(5, 2, 'A', 600.00,  8000),
(5, 2, 'B',  1500.00, 4000),
(1, 1, 'D',  4000.00, 200),
(2, 2, 'D',  4500.00, 150);

-- Comision
INSERT IGNORE INTO Comision (IdComision, Vigente, Fecha, Porcentaje) VALUES
(1, false, '2024-06-01', 3.50),
(2, false, '2024-06-15', 4.00),
(3, true,  '2024-07-01', 5.00);

-- QR
INSERT IGNORE INTO QR (IdQR) VALUES
('QR-001'), ('QR-002'), ('QR-003'), ('QR-004'), ('QR-005'),
('QR-006'), ('QR-007'), ('QR-008'), ('QR-009'), ('QR-010'),
('QR-011'), ('QR-012'), ('QR-013'), ('QR-014'), ('QR-015');

-- Compra
INSERT IGNORE INTO Compra (Id, Estado, Fecha, MontoTotal, PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario, IdComision) VALUES
(1, 'Completada', '2024-05-10', 1500.00, 'URY', 'CI', '12345678', 1),
(2, 'Completada', '2024-05-12', 2400.00, 'URY', 'CI', '34567890', 2),
(3, 'Pendiente',  '2024-05-20', 800.00,  'URY', 'CI', '12345678', 3),
(4, 'Completada', '2024-06-01', 3200.00, 'URY', 'CI', '56789012', 3),
(5, 'Cancelada',  '2024-06-15', 600.00,  'URY', 'CI', '78901234', 3),
(6, 'Completada', '2024-06-20', 5000.00, 'URY', 'CI', '34567890', 3);

-- Entrada
INSERT IGNORE INTO Entrada (IdEntrada, Estado, NumeroVecesTransferida, IdCompra, IdEvento, IdEstadio, Tipo, IdQR) VALUES
(1,  'Activa',      0, 1, 1, 1, 'A', 'QR-001'),
(2,  'Activa',      0, 1, 1, 1, 'A', 'QR-002'),
(3,  'Transferida', 1, 1, 1, 1, 'A', 'QR-003'),
(4,  'Activa',      0, 2, 2, 2, 'B',  'QR-004'),
(5,  'Usada',       0, 2, 2, 2, 'B',  'QR-005'),
(6,  'Activa',      0, 3, 1, 1, 'A', 'QR-006'),
(7,  'Cancelada',   0, 3, 1, 1, 'A', 'QR-007'),
(8,  'Activa',      0, 4, 4, 3, 'A', 'QR-008'),
(9,  'Activa',      0, 4, 4, 3, 'A', 'QR-009'),
(10, 'Usada',       0, 4, 4, 3, 'A', 'QR-010'),
(11, 'Usada',       0, 4, 4, 3, 'A', 'QR-011'),
(12, 'Cancelada',   0, 5, 5, 2, 'A', 'QR-012'),
(13, 'Cancelada',   0, 5, 5, 2, 'A', 'QR-013'),
(14, 'Activa',      0, 6, 2, 2, 'B',  'QR-014'),
(15, 'Activa',      0, 6, 2, 2, 'B',  'QR-015');

-- Transferencia
INSERT IGNORE INTO Transferencia (IdTransferencia, FechaTransferencia, EstadoTransferencia, IdEntrada, PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario) VALUES
(1, '2024-05-15', 'Aceptada',  3, 'URY', 'CI', '34567890'),
(2, '2024-05-18', 'Pendiente', 3, 'URY', 'CI', '56789012'),
(3, '2024-05-25', 'Aceptada',  1, 'URY', 'CI', '78901234'),
(4, '2024-06-05', 'Pendiente', 9, 'URY', 'CI', '12345678');

-- Asignado_a
INSERT IGNORE INTO Asignado_a (PaisDocFunc, TipoDocFunc, NumeroDocFunc, IdEvento, IdEstadio, Tipo) VALUES
('URY', 'CI', '89012345', 1, 1, 'A'),
('URY', 'CI', '89012345', 1, 1, 'B'),
('URY', 'CI', '90123456', 2, 2, 'A'),
('URY', 'CI', '01234567', 4, 3, 'A');
