import { fetchJson } from './api';

export function getAll() {
  return fetchJson('/api/estadios');
}

export function getByAdmin(paisDoc, tipoDoc, numeroDoc) {
  return fetchJson('/api/estadios', {
    params: { paisDoc, tipoDoc, numeroDoc },
  });
}
