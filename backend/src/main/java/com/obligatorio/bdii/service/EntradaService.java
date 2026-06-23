package com.obligatorio.bdii.service;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import com.obligatorio.bdii.dto.ValidarEntradaRequest;
import com.obligatorio.bdii.model.Entrada;
import com.obligatorio.bdii.model.SectorTipo;

@Service
public class EntradaService {

    private final JdbcTemplate jdbcTemplate;

    public EntradaService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Entrada> obtenerEntradasPorUsuario(String paisDocUsuario, String tipoDocUsuario, String numeroDocUsuario){
        String sql = "SELECT e.IdEntrada, e.Estado, e.NumeroVecesTransferida, e.IdCompra, e.IdEvento, e.IdEstadio, e.Tipo, e.IdQR " +
             "FROM Entrada e JOIN Compra c ON e.IdCompra = c.Id " +
             "WHERE c.PaisDocUsuario = ? AND c.TipoDocUsuario = ? AND c.NumeroDocUsuario = ? " +
             "AND e.Estado != 'Transferida' " +
             "UNION " +
             "SELECT e.IdEntrada, e.Estado, e.NumeroVecesTransferida, e.IdCompra, e.IdEvento, e.IdEstadio, e.Tipo, e.IdQR " +
             "FROM Entrada e JOIN Transferencia t ON e.IdEntrada = t.IdEntrada " +
             "WHERE t.PaisDocUsuario = ? AND t.TipoDocUsuario = ? AND t.NumeroDocUsuario = ? " +
             "AND t.EstadoTransferencia = 'Aceptada'";

         return jdbcTemplate.query(sql,(rs,rowNum)->{
            Entrada e = new Entrada();
            e.setIdEntrada(rs.getInt("idEntrada"));
            e.setEstado(rs.getString("estado"));
            e.setNumeroVecesTransferida(rs.getInt("numeroVecesTransferida"));
            e.setIdCompra(rs.getInt("idCompra"));
            e.setIdEvento(rs.getInt("idEvento"));
            e.setIdEstadio(rs.getInt("idEstadio"));
            e.setTipo(SectorTipo.valueOf(rs.getString("tipo")));
            e.setIdQR(rs.getString("idQR"));
            return e;
         },paisDocUsuario,tipoDocUsuario,numeroDocUsuario, paisDocUsuario, tipoDocUsuario, numeroDocUsuario);
        }

    public ResponseEntity<?> validarEntrada(ValidarEntradaRequest request) {
        // Buscar la entrada por el QR escaneado
        List<Entrada> entradas = jdbcTemplate.query(
            "SELECT IdEntrada, Estado, IdEvento, IdEstadio, Tipo FROM Entrada WHERE IdQR = ?",
            (rs, rowNum) -> {
                Entrada e = new Entrada();
                e.setIdEntrada(rs.getInt("IdEntrada"));
                e.setEstado(rs.getString("Estado"));
                e.setIdEvento(rs.getInt("IdEvento"));
                e.setIdEstadio(rs.getInt("IdEstadio"));
                e.setTipo(SectorTipo.valueOf(rs.getString("Tipo")));
                return e;
            },
            request.IdQR()
        );

        if (entradas.isEmpty()) {
            return ResponseEntity.badRequest().body("QR inválido: no corresponde a ninguna entrada");
        }

        Entrada entrada = entradas.get(0);

        if (!"Activa".equals(entrada.getEstado())) {
            return ResponseEntity.badRequest().body("La entrada no está activa (estado: " + entrada.getEstado() + ")");
        }

        // Verificar que el funcionario está asignado al sector de esta entrada
        Integer asignado = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Asignado_a WHERE PaisDocFunc = ? AND TipoDocFunc = ? AND NumeroDocFunc = ? AND IdEvento = ? AND IdEstadio = ? AND Tipo = ?",
            Integer.class,
            request.paisDocFuncionario(), request.tipoDocFuncionario(), request.numeroDocFuncionario(),
            entrada.getIdEvento(), entrada.getIdEstadio(), entrada.getTipo().name()
        );

        if (asignado == null || asignado == 0) {
            return ResponseEntity.badRequest().body("El funcionario no está asignado al sector de esta entrada");
        }

        // Buscar el dispositivo asignado al funcionario logueado
        List<Integer> dispositivos = jdbcTemplate.query(
            "SELECT IdDispositivo FROM Dispositivo_Validacion WHERE PaisDocFunc = ? AND TipoDocFunc = ? AND NumeroDocFunc = ?",
            (rs, rowNum) -> rs.getInt("IdDispositivo"),
            request.paisDocFuncionario(),
            request.tipoDocFuncionario(),
            request.numeroDocFuncionario()
        );

        if (dispositivos.isEmpty()) {
            return ResponseEntity.badRequest().body("El funcionario no tiene un dispositivo de validación asignado");
        }

        Integer idDispositivo = dispositivos.get(0);

        // Marcar la entrada como consumida y registrar la auditoría
        jdbcTemplate.update(
            "UPDATE Entrada SET Estado = 'Consumida', IdDispositivoValidacion = ?, CodigoAceptado = ?, FechaHoraValidacion = NOW() WHERE IdEntrada = ?",
            idDispositivo,
            request.IdQR(),
            entrada.getIdEntrada()
        );

        return ResponseEntity.ok("Entrada validada correctamente");
    }

    public ResponseEntity<?> renovarQR(Integer idEntrada) {
        // Verificar que la entrada existe y está activa
        List<String> qrActual = jdbcTemplate.query(
            "SELECT IdQR FROM Entrada WHERE IdEntrada = ? AND Estado = 'Activa'",
            (rs, rowNum) -> rs.getString("IdQR"),
            idEntrada
        );

        if (qrActual.isEmpty()) {
            return ResponseEntity.badRequest().body("Entrada no encontrada o no está activa");
        }

        String qrViejo = qrActual.get(0);
        String qrNuevo = UUID.randomUUID().toString();

        // Insertar el nuevo QR y actualizar la entrada
        jdbcTemplate.update("INSERT INTO QR (IdQR) VALUES (?)", qrNuevo);
        jdbcTemplate.update("UPDATE Entrada SET IdQR = ? WHERE IdEntrada = ?", qrNuevo, idEntrada);
        // Eliminar el QR viejo para no acumular basura
        jdbcTemplate.update("DELETE FROM QR WHERE IdQR = ?", qrViejo);

        return ResponseEntity.ok(qrNuevo);
    }

}
