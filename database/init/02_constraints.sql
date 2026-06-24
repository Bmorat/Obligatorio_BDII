-- ============================================================
-- 02_constraints.sql
-- Restricciones no estructurales del modelo
-- Ejecutar DESPUÉS de 01_schema.sql y data.sql
-- Para que Spring lo ejecute automático, agregar a
-- application.properties:
--   spring.sql.init.schema-locations=classpath:schema.sql,classpath:02_constraints.sql
-- ============================================================

-- ============================================================
-- 1. CAMBIOS DE COLUMNA
-- ============================================================

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
  CHECK (Estado IN ('Pendiente', 'Completada', 'Cancelada'));

ALTER TABLE Entrada ADD CONSTRAINT CK_Entrada_Estado
  CHECK (Estado IN ('Activa', 'Transferida', 'Usada', 'Cancelada'));

ALTER TABLE Transferencia ADD CONSTRAINT CK_Transferencia_Estado
  CHECK (EstadoTransferencia IN ('Pendiente', 'Aceptada', 'Rechazada'));

ALTER TABLE Entrada ADD CONSTRAINT CK_Entrada_VecesTransferida
  CHECK (NumeroVecesTransferida BETWEEN 0 AND 3);

-- 4. TRIGGERS — roles exclusivos

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

-- ============================================================
-- 5. TRIGGERS — reglas de negocio
-- ============================================================

-- 5a. CapacidadHabilitada ≤ Capacidad del sector
CREATE TRIGGER TRG_SeHabilita_CheckCapacidad
BEFORE INSERT ON Se_habilita
FOR EACH ROW
BEGIN
  DECLARE cap INT;
  SELECT Capacidad INTO cap FROM Sector
    WHERE IdEstadio = NEW.IdEstadio AND Tipo = NEW.Tipo;
  IF NEW.CapacidadHabilitada > cap THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'CapacidadHabilitada no puede superar la Capacidad del sector';
  END IF;
END//

CREATE TRIGGER TRG_SeHabilita_CheckCapacidad_Update
BEFORE UPDATE ON Se_habilita
FOR EACH ROW
BEGIN
  DECLARE cap INT;
  SELECT Capacidad INTO cap FROM Sector
    WHERE IdEstadio = NEW.IdEstadio AND Tipo = NEW.Tipo;
  IF NEW.CapacidadHabilitada > cap THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'CapacidadHabilitada no puede superar la Capacidad del sector';
  END IF;
END//

-- 5b. IdEstadio en Se_habilita debe coincidir con el del Evento
CREATE TRIGGER TRG_SeHabilita_CheckEstadio
BEFORE INSERT ON Se_habilita
FOR EACH ROW
BEGIN
  DECLARE evEstadio INT;
  SELECT IdEstadio INTO evEstadio FROM Evento WHERE Id = NEW.IdEvento;
  IF evEstadio != NEW.IdEstadio THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'El sector debe pertenecer al estadio del evento';
  END IF;
END//

CREATE TRIGGER TRG_SeHabilita_CheckEstadio_Update
BEFORE UPDATE ON Se_habilita
FOR EACH ROW
BEGIN
  DECLARE evEstadio INT;
  SELECT IdEstadio INTO evEstadio FROM Evento WHERE Id = NEW.IdEvento;
  IF evEstadio != NEW.IdEstadio THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'El sector debe pertenecer al estadio del evento';
  END IF;
END//

-- 5c. Máximo 5 entradas por compra
CREATE TRIGGER TRG_Entrada_CheckMaxPorCompra
BEFORE INSERT ON Entrada
FOR EACH ROW
BEGIN
  DECLARE cnt INT;
  SELECT COUNT(*) INTO cnt FROM Entrada WHERE IdCompra = NEW.IdCompra;
  IF cnt >= 5 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'No se pueden agregar más de 5 entradas a una compra';
  END IF;
END//

-- 5d. Solo entradas activas pueden transferirse
CREATE TRIGGER TRG_Transferencia_CheckEntradaActiva
BEFORE INSERT ON Transferencia
FOR EACH ROW
BEGIN
  DECLARE est VARCHAR(30);
  SELECT Estado INTO est FROM Entrada WHERE IdEntrada = NEW.IdEntrada;
  IF est IS NULL OR est != 'Activa' THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Solo entradas activas pueden transferirse';
  END IF;
END//

-- 5e. Estado de entrada consumida/cancelada es irreversible
CREATE TRIGGER TRG_Entrada_CheckEstadoIrreversible
BEFORE UPDATE ON Entrada
FOR EACH ROW
BEGIN
  IF OLD.Estado IN ('Usada', 'Cancelada') AND NEW.Estado != OLD.Estado THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'No se puede cambiar el estado de una entrada usada o cancelada';
  END IF;
END//

-- 5f. No vender más entradas que la capacidad habilitada del sector
CREATE TRIGGER TRG_Entrada_CheckCapacidadSector
BEFORE INSERT ON Entrada
FOR EACH ROW
BEGIN
  DECLARE cap INT;
  DECLARE vendidas INT;
  SELECT CapacidadHabilitada INTO cap FROM Se_habilita
    WHERE IdEvento = NEW.IdEvento AND IdEstadio = NEW.IdEstadio AND Tipo = NEW.Tipo;
  SELECT COUNT(*) INTO vendidas FROM Entrada
    WHERE IdEvento = NEW.IdEvento AND IdEstadio = NEW.IdEstadio AND Tipo = NEW.Tipo;
  IF vendidas >= cap THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'No hay capacidad disponible en este sector para el evento';
  END IF;
END//

CREATE TRIGGER TRG_Entrada_CheckCapacidadSector_Update
BEFORE UPDATE ON Entrada
FOR EACH ROW
BEGIN
  DECLARE cap INT;
  DECLARE vendidas INT;
  SELECT CapacidadHabilitada INTO cap FROM Se_habilita
    WHERE IdEvento = NEW.IdEvento AND IdEstadio = NEW.IdEstadio AND Tipo = NEW.Tipo;
  SELECT COUNT(*) INTO vendidas FROM Entrada
    WHERE IdEvento = NEW.IdEvento AND IdEstadio = NEW.IdEstadio AND Tipo = NEW.Tipo;
  IF vendidas >= cap THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'No hay capacidad disponible en este sector para el evento';
  END IF;
END//

-- 5g. No superposición de eventos en mismo estadio (±90 min)
CREATE TRIGGER TRG_Evento_CheckHorario
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
  DECLARE conflict INT;
  SELECT COUNT(*) INTO conflict FROM Evento
    WHERE IdEstadio = NEW.IdEstadio
      AND Fecha = NEW.Fecha
      AND ABS(TIMESTAMPDIFF(MINUTE, Hora, NEW.Hora)) < 90;
  IF conflict > 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Ya existe un evento en este estadio con menos de 90 min de diferencia';
  END IF;
END//

CREATE TRIGGER TRG_Evento_CheckHorario_Update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
  DECLARE conflict INT;
  SELECT COUNT(*) INTO conflict FROM Evento
    WHERE IdEstadio = NEW.IdEstadio
      AND Fecha = NEW.Fecha
      AND Id != NEW.Id
      AND ABS(TIMESTAMPDIFF(MINUTE, Hora, NEW.Hora)) < 90;
  IF conflict > 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Ya existe un evento en este estadio con menos de 90 min de diferencia';
  END IF;
END//

DELIMITER ;
