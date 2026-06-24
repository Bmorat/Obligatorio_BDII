import { fetchJson } from './api';
import * as estadioService from './estadioService';
import * as eventoService from './eventoService';

export async function getCartelera() {
  const estadios = await estadioService.getAll();

  const grupos = await Promise.all(
    estadios.map(async (estadio) => {
      const eventos = await eventoService.getEventosPorEstadio(estadio.id);

      const eventosCompletos = await Promise.all(
        eventos.map(async (evento) => {
          const [equipos, sectores] = await Promise.all([
            eventoService.getEquiposPorEvento(evento.id),
            eventoService.getSectores(evento.id),
          ]);

          return {
            ...evento,
            estadio,
            sectores,
            local: equipos.find((equipo) => equipo.rol === 'Local'),
            visitante: equipos.find((equipo) => equipo.rol === 'Visitante'),
          };
        })
      );

      return eventosCompletos;
    })
  );

  return grupos.flat().sort((a, b) => {
    const fechaA = `${a.fecha}T${a.hora}`;
    const fechaB = `${b.fecha}T${b.hora}`;
    return fechaA.localeCompare(fechaB);
  });
}

export function comprarEntrada(session, seleccion) {
  return fetchJson('/api/compras', {
    method: 'POST',
    body: {
      paisDocUsuario: session.paisDoc,
      tipoDocUsuario: session.tipoDoc,
      numeroDocUsuario: session.numeroDoc,
      idEvento: seleccion.evento.id,
      idEstadio: seleccion.evento.estadio.id,
      tipo: seleccion.tipo,
      cantidad: Number(seleccion.cantidad),
    },
  });
}

export function getCompras(session) {
  return fetchJson('/api/compras', {
    params: documentoParams(session),
  });
}

export function getEntradas(session) {
  return fetchJson('/api/entradas', {
    params: documentoParams(session),
  });
}

export function getTransferenciasRecibidas(session) {
  return fetchJson('/api/transferencias/recibidas', {
    params: documentoParams(session),
  });
}

export function transferirEntrada(idEntrada, destinatario) {
  return fetchJson('/api/transferencias', {
    method: 'POST',
    body: {
      idEntrada,
      paisDocDestinatario: destinatario.paisDoc,
      tipoDocDestinatario: destinatario.tipoDoc,
      numeroDocDestinatario: destinatario.numeroDoc,
    },
  });
}

export function aceptarTransferencia(idTransferencia) {
  return fetchJson(`/api/transferencias/${idTransferencia}/aceptar`, {
    method: 'PUT',
  });
}

export function rechazarTransferencia(idTransferencia) {
  return fetchJson(`/api/transferencias/${idTransferencia}/rechazar`, {
    method: 'PUT',
  });
}

function documentoParams(session) {
  return {
    paisDoc: session.paisDoc,
    tipoDoc: session.tipoDoc,
    numeroDoc: session.numeroDoc,
  };
}
