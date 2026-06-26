

-- 1a. Usuario_General.EstadoVerificacion como BOOLEAN 
DELIMITER //
CREATE PROCEDURE sp_migrar_estado_verificacion()
BEGIN
  DECLARE col_type VARCHAR(64);
  SELECT DATA_TYPE INTO col_type
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'Usuario_General'
      AND COLUMN_NAME = 'EstadoVerificacion';
  IF col_type = 'varchar' THEN
    ALTER TABLE Usuario_General ADD COLUMN verificado_temp BOOLEAN NOT NULL DEFAULT FALSE;
    UPDATE Usuario_General SET verificado_temp = (EstadoVerificacion = 'Verificado');
    ALTER TABLE Usuario_General DROP COLUMN EstadoVerificacion;
    ALTER TABLE Usuario_General RENAME COLUMN verificado_temp TO EstadoVerificacion;
  END IF;
END//
CALL sp_migrar_estado_verificacion()//
DROP PROCEDURE sp_migrar_estado_verificacion//
DELIMITER ;

-- ============================================================
-- 2. CHECK CONSTRAINTS
-- ============================================================

ALTER TABLE Compra ADD CONSTRAINT CK_Compra_Estado
  CHECK (Estado IN ('Pendiente', 'Completada', 'Cancelada', 'Confirmada'));

ALTER TABLE Entrada ADD CONSTRAINT CK_Entrada_Estado
  CHECK (Estado IN ('Activa', 'Usada', 'Cancelada'));

ALTER TABLE Transferencia ADD CONSTRAINT CK_Transferencia_Estado
  CHECK (EstadoTransferencia IN ('Pendiente', 'Aceptada', 'Rechazada'));

ALTER TABLE Entrada ADD CONSTRAINT CK_Entrada_VecesTransferida
  CHECK (NumeroVecesTransferida BETWEEN 0 AND 3);

ALTER TABLE Juega ADD CONSTRAINT CK_Juega_Rol
  CHECK (Rol IN ('Local', 'Visitante'));

-- 4. TRIGGERS — roles exclusivos

DELIMITER //

CREATE TRIGGER TRG_UsuarioGeneral_UnicoTipo
BEFORE INSERT ON Usuario_General
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM Administrador_Sede
        WHERE PaisDoc = NEW.PaisDoc AND TipoDoc = NEW.TipoDoc AND NumeroDoc = NEW.NumeroDoc
    ) OR EXISTS (
        SELECT 1 FROM Funcionario_Validacion
        WHERE PaisDoc = NEW.PaisDoc AND TipoDoc = NEW.TipoDoc AND NumeroDoc = NEW.NumeroDoc
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario ya tiene otro tipo asignado';
    END IF;
END//

CREATE TRIGGER TRG_AdministradorSede_UnicoTipo
BEFORE INSERT ON Administrador_Sede
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM Usuario_General
        WHERE PaisDoc = NEW.PaisDoc AND TipoDoc = NEW.TipoDoc AND NumeroDoc = NEW.NumeroDoc
    ) OR EXISTS (
        SELECT 1 FROM Funcionario_Validacion
        WHERE PaisDoc = NEW.PaisDoc AND TipoDoc = NEW.TipoDoc AND NumeroDoc = NEW.NumeroDoc
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario ya tiene otro tipo asignado';
    END IF;
END//

CREATE TRIGGER TRG_FuncionarioValidacion_UnicoTipo
BEFORE INSERT ON Funcionario_Validacion
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM Usuario_General
        WHERE PaisDoc = NEW.PaisDoc AND TipoDoc = NEW.TipoDoc AND NumeroDoc = NEW.NumeroDoc
    ) OR EXISTS (
        SELECT 1 FROM Administrador_Sede
        WHERE PaisDoc = NEW.PaisDoc AND TipoDoc = NEW.TipoDoc AND NumeroDoc = NEW.NumeroDoc
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario ya tiene otro tipo asignado';
    END IF;
END//

-- ============================================================
-- 5. STORED PROCEDURES — lógica compartida
-- ============================================================

-- 5a. CapacidadHabilitada ≤ Capacidad del sector
CREATE PROCEDURE sp_check_capacidad(IN p_idestadio INT, IN p_tipo VARCHAR(1), IN p_capacidad INT)
BEGIN
  DECLARE cap INT;
  SELECT Capacidad INTO cap FROM Sector WHERE IdEstadio = p_idestadio AND Tipo = p_tipo;
  IF p_capacidad > cap THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'CapacidadHabilitada no puede superar la Capacidad del sector';
  END IF;
END//

-- 5b. IdEstadio en Se_habilita debe coincidir con el del Evento
CREATE PROCEDURE sp_check_estadio(IN p_idevento INT, IN p_idestadio INT)
BEGIN
  DECLARE evEstadio INT;
  SELECT IdEstadio INTO evEstadio FROM Evento WHERE Id = p_idevento;
  IF evEstadio != p_idestadio THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El sector debe pertenecer al estadio del evento';
  END IF;
END//

-- 5c. No vender más entradas que la capacidad habilitada
CREATE PROCEDURE sp_check_capacidad_entrada(IN p_idevento INT, IN p_idestadio INT, IN p_tipo VARCHAR(1))
BEGIN
  DECLARE cap INT;
  DECLARE vendidas INT;
  SELECT CapacidadHabilitada INTO cap FROM Se_habilita
    WHERE IdEvento = p_idevento AND IdEstadio = p_idestadio AND Tipo = p_tipo;
  SELECT COUNT(*) INTO vendidas FROM Entrada
    WHERE IdEvento = p_idevento AND IdEstadio = p_idestadio AND Tipo = p_tipo;
  IF vendidas >= cap THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No hay capacidad disponible en este sector para el evento';
  END IF;
END//

-- 5d. No superposición de eventos en mismo estadio (±90 min)
-- p_id = NEW.Id (para UPDATE) o NULL (para INSERT, se chequean todos)
CREATE PROCEDURE sp_check_horario(IN p_id INT, IN p_fecha DATE, IN p_hora TIME, IN p_idestadio INT)
BEGIN
  DECLARE conflict INT;
  SELECT COUNT(*) INTO conflict FROM Evento
    WHERE IdEstadio = p_idestadio
      AND Fecha = p_fecha
      AND (p_id IS NULL OR Id != p_id)
      AND ABS(TIMESTAMPDIFF(MINUTE, Hora, p_hora)) < 90;
  IF conflict > 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe un evento en este estadio con menos de 90 min de diferencia';
  END IF;
END//

-- ============================================================
-- 6. TRIGGERS — reglas de negocio
-- ============================================================

-- 6a. CapacidadHabilitada ≤ Capacidad del sector
CREATE TRIGGER TRG_SeHabilita_CheckCapacidad
BEFORE INSERT ON Se_habilita
FOR EACH ROW
BEGIN
  CALL sp_check_capacidad(NEW.IdEstadio, NEW.Tipo, NEW.CapacidadHabilitada);
END//

CREATE TRIGGER TRG_SeHabilita_CheckCapacidad_Update
BEFORE UPDATE ON Se_habilita
FOR EACH ROW
BEGIN
  CALL sp_check_capacidad(NEW.IdEstadio, NEW.Tipo, NEW.CapacidadHabilitada);
END//

-- 6b. IdEstadio en Se_habilita debe coincidir con el del Evento
CREATE TRIGGER TRG_SeHabilita_CheckEstadio
BEFORE INSERT ON Se_habilita
FOR EACH ROW
BEGIN
  CALL sp_check_estadio(NEW.IdEvento, NEW.IdEstadio);
END//

CREATE TRIGGER TRG_SeHabilita_CheckEstadio_Update
BEFORE UPDATE ON Se_habilita
FOR EACH ROW
BEGIN
  CALL sp_check_estadio(NEW.IdEvento, NEW.IdEstadio);
END//

-- 6c. Máximo 5 entradas por compra
CREATE TRIGGER TRG_Entrada_CheckMaxPorCompra
BEFORE INSERT ON Entrada
FOR EACH ROW
BEGIN
  DECLARE cnt INT;
  SELECT COUNT(*) INTO cnt FROM Entrada WHERE IdCompra = NEW.IdCompra;
  IF cnt >= 5 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pueden agregar más de 5 entradas a una compra';
  END IF;
END//

-- 6d. Solo entradas activas pueden transferirse
CREATE TRIGGER TRG_Transferencia_CheckEntradaActiva
BEFORE INSERT ON Transferencia
FOR EACH ROW
BEGIN
  DECLARE est VARCHAR(30);
  SELECT Estado INTO est FROM Entrada WHERE IdEntrada = NEW.IdEntrada;
  IF est IS NULL OR est != 'Activa' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo entradas activas pueden transferirse';
  END IF;
END//

-- 6e. Estado de entrada consumida/cancelada es irreversible
CREATE TRIGGER TRG_Entrada_CheckEstadoIrreversible
BEFORE UPDATE ON Entrada
FOR EACH ROW
BEGIN
  IF OLD.Estado IN ('Usada', 'Cancelada') AND NEW.Estado != OLD.Estado THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede cambiar el estado de una entrada usada o cancelada';
  END IF;
END//

-- 6f. No vender más entradas que la capacidad habilitada del sector
CREATE TRIGGER TRG_Entrada_CheckCapacidadSector
BEFORE INSERT ON Entrada
FOR EACH ROW
BEGIN
  CALL sp_check_capacidad_entrada(NEW.IdEvento, NEW.IdEstadio, NEW.Tipo);
END//

CREATE TRIGGER TRG_Entrada_CheckCapacidadSector_Update
BEFORE UPDATE ON Entrada
FOR EACH ROW
BEGIN
  CALL sp_check_capacidad_entrada(NEW.IdEvento, NEW.IdEstadio, NEW.Tipo);
END//

-- 6g. No superposición de eventos en mismo estadio (±90 min)
CREATE TRIGGER TRG_Evento_CheckHorario
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
  CALL sp_check_horario(NULL, NEW.Fecha, NEW.Hora, NEW.IdEstadio);
END//

CREATE TRIGGER TRG_Evento_CheckHorario_Update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
  CALL sp_check_horario(NEW.Id, NEW.Fecha, NEW.Hora, NEW.IdEstadio);
END//

-- 6h. Un equipo no puede participar en dos eventos el mismo día
CREATE TRIGGER TRG_Juega_CheckEquipoMismoDia
BEFORE INSERT ON Juega
FOR EACH ROW
BEGIN
  DECLARE conflict INT;
  SELECT COUNT(*) INTO conflict
  FROM Juega j
  INNER JOIN Evento e ON j.IdEvento = e.Id
  WHERE j.IdEquipo = NEW.IdEquipo
    AND j.IdEvento != NEW.IdEvento
    AND e.Fecha = (SELECT Fecha FROM Evento WHERE Id = NEW.IdEvento);
  IF conflict > 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El equipo ya participa en otro evento en esta fecha';
  END IF;
END//

CREATE TRIGGER TRG_Juega_CheckEquipoMismoDia_Update
BEFORE UPDATE ON Juega
FOR EACH ROW
BEGIN
  DECLARE conflict INT;
  SELECT COUNT(*) INTO conflict
  FROM Juega j
  INNER JOIN Evento e ON j.IdEvento = e.Id
  WHERE j.IdEquipo = NEW.IdEquipo
    AND j.IdEvento != NEW.IdEvento
    AND e.Fecha = (SELECT Fecha FROM Evento WHERE Id = NEW.IdEvento);
  IF conflict > 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El equipo ya participa en otro evento en esta fecha';
  END IF;
END//

DELIMITER ;
