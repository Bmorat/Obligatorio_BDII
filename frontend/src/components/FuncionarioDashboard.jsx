import React, { useState, useRef } from 'react';
import { clearSession } from '../services/api';
import { validarEntrada } from '../services/entradaService';

export default function FuncionarioDashboard({ session, onLogout }) {
  const [qrInput, setQrInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState(null);
  const [error, setError] = useState('');
  const inputRef = useRef(null);

  const roleLabel = session.rol.replace('ROLE_', '').replaceAll('_', ' ');

  function handleLogout() {
    clearSession();
    onLogout();
  }

  async function handleValidar(e) {
    e.preventDefault();
    const qr = qrInput.trim();
    if (!qr) return;

    setLoading(true);
    setError('');
    setResultado(null);

    try {
      const res = await validarEntrada(session, qr);
      setResultado({ qr, mensaje: res });
      setQrInput('');
      inputRef.current?.focus();
    } catch (err) {
      if (err.message.includes('401') || err.message.includes('403')) {
        clearSession();
        onLogout();
        return;
      }
      setError(err.message);
      setResultado(null);
      inputRef.current?.focus();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="admin-shell">
      <header className="admin-header">
        <div>
          <span className="eyebrow">Obligatorio BDII</span>
          <h1>Panel de Funcionario de Validacion</h1>
        </div>
        <div className="admin-header-right">
          <span className="admin-user">{session.correo}</span>
          <span className="admin-role-badge">{roleLabel}</span>
          <button className="secondary-button" type="button" onClick={handleLogout}>
            Cerrar sesion
          </button>
        </div>
      </header>

      <div className="admin-layout">
        <main className="admin-main admin-main--centered">
          <div className="validation-card">
            <form onSubmit={handleValidar}>
              <label htmlFor="qr-input">Codigo QR de la entrada</label>
              <input
                id="qr-input"
                ref={inputRef}
                type="text"
                className="qr-input"
                placeholder="Ingrese o escanee el codigo QR"
                value={qrInput}
                onChange={(e) => setQrInput(e.target.value)}
                autoFocus
                disabled={loading}
              />
              <button
                className="primary-button"
                type="submit"
                disabled={loading || !qrInput.trim()}
              >
                {loading ? 'Validando...' : 'Validar Entrada'}
              </button>
            </form>
          </div>

          {resultado && (
            <div className="validation-result validation-result--ok">
              <strong className="validation-result-title">Entrada validada correctamente</strong>
              <dl className="validation-result-meta">
                <div>
                  <dt>QR</dt>
                  <dd>{resultado.qr}</dd>
                </div>
                {typeof resultado.mensaje === 'string' && (
                  <div>
                    <dt>Respuesta</dt>
                    <dd>{resultado.mensaje}</dd>
                  </div>
                )}
              </dl>
            </div>
          )}

          {error && (
            <div className="validation-result validation-result--err">
              <strong className="validation-result-title">Error al validar</strong>
              <p className="validation-result-msg">{error}</p>
            </div>
          )}
        </main>
      </div>
    </div>
  );
}
