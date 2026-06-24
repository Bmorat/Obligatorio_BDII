import { fetchJson } from './api';

export function getEventosMasVendidos() {
  return fetchJson('/api/reportes/eventos-mas-vendidos');
}

export function getMayoresCompradores() {
  return fetchJson('/api/reportes/mayores-compradores');
}
