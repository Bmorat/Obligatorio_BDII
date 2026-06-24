import { fetchJson } from './api';

export function getEventosPorEstadio(idEstadio) {
  return fetchJson(`/api/estadios/${idEstadio}/eventos`);
}

export function getEquipos() {
  return fetchJson('/api/equipos');
}

export function getEquiposPorEvento(idEvento) {
  return fetchJson(`/api/equipos/evento/${idEvento}`);
}

export function crearEvento(data) {
  return fetchJson('/api/eventos', {
    method: 'POST',
    params: {
      fecha: data.fecha,
      hora: data.hora,
      idEstadio: data.idEstadio,
      idEquipoLocal: data.idEquipoLocal,
      idEquipoVisitante: data.idEquipoVisitante,
    },
  });
}

export function actualizarEvento(id, data) {
  const params = {
    fecha: data.fecha,
    hora: data.hora,
    idEstadio: data.idEstadio,
  };
  if (data.idEquipoLocal != null && data.idEquipoVisitante != null) {
    params.idEquipoLocal = data.idEquipoLocal;
    params.idEquipoVisitante = data.idEquipoVisitante;
  }
  return fetchJson(`/api/eventos/${id}`, { method: 'PATCH', params });
}

export function eliminarEvento(id) {
  return fetchJson(`/api/eventos/${id}`, { method: 'DELETE' });
}

export function getSectores(idEvento) {
  return fetchJson(`/api/eventos/${idEvento}/sectores`);
}

export function getCapacidadesSectores(estadioId) {
  return fetchJson(`/api/sectores?estadioId=${estadioId}`);
}



export function habilitarSector(data) {
  return fetchJson('/api/SeHablita', {
    method: 'POST',
    params: {
      IdEvento: data.idEvento,
      Tipo: data.tipo,
      Precio: data.precio,
      CapacidadMax: data.capacidadHabilitada,
    },
  });
}

export function deshabilitarSector(data) {
  return fetchJson('/api/SeHablita', {
    method: 'DELETE',
    params: {
      IdEvento: data.idEvento,
      Tipo: data.tipo,
    },
  });
}

export function deshabilitarSectorForzar(data) {
  return fetchJson('/api/SeHablita/forzar', {
    method: 'DELETE',
    params: {
      IdEvento: data.idEvento,
      Tipo: data.tipo,
    },
  });
}
