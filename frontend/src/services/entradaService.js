import { fetchJson } from './api';

export async function validarEntrada(session, idQR) {
  return fetchJson('/api/entradas/validar', {
    method: 'POST',
    body: {
      IdQR: idQR,
      paisDocFuncionario: session.paisDoc,
      tipoDocFuncionario: session.tipoDoc,
      numeroDocFuncionario: session.numeroDoc,
    },
  });
}
