import React, { useState } from 'react';
import * as usuarioService from '../services/usuarioService';

export default function TransferModal({ entrada, onClose, onTransferred }) {
  const [destinatario, setDestinatario] = useState({
    paisDoc: 'URY',
    tipoDoc: 'CI',
    numeroDoc: '',
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  function handleChange(event) {
    const { name, value } = event.target;
    setDestinatario((actual) => ({ ...actual, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setError('');

    try {
      await usuarioService.transferirEntrada(entrada.idEntrada, destinatario);
      onTransferred();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(event) => event.stopPropagation()}>
        <div className="modal-header">
          <div>
            <p className="eyebrow">Transferir entrada</p>
            <h3>Entrada #{entrada.idEntrada}</h3>
          </div>
          <button className="modal-close" type="button" onClick={onClose} aria-label="Cerrar">x</button>
        </div>

        <form onSubmit={handleSubmit}>
          <label>
            Pais del documento
            <input name="paisDoc" value={destinatario.paisDoc} onChange={handleChange} required />
          </label>
          <label>
            Tipo de documento
            <input name="tipoDoc" value={destinatario.tipoDoc} onChange={handleChange} required />
          </label>
          <label>
            Numero de documento
            <input name="numeroDoc" value={destinatario.numeroDoc} onChange={handleChange} required />
          </label>

          {error && <p className="error-message">{error}</p>}

          <div className="modal-actions">
            <button className="secondary-button" type="button" onClick={onClose}>Cancelar</button>
            <button className="primary-button" type="submit" disabled={saving}>
              {saving ? 'Enviando...' : 'Enviar transferencia'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
