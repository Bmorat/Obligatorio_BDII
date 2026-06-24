import React, { useMemo, useState } from 'react';
import * as usuarioService from '../services/usuarioService';

export default function PurchaseModal({ evento, session, onClose, onPurchased }) {
  const [tipo, setTipo] = useState(evento.sectores[0]?.tipo || '');
  const [cantidad, setCantidad] = useState(1);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  const sector = useMemo(
    () => evento.sectores.find((item) => item.tipo === tipo),
    [evento.sectores, tipo]
  );
  const totalEstimado = sector ? Number(sector.precio) * Number(cantidad) * 1.05 : 0;

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setError('');

    try {
      await usuarioService.comprarEntrada(session, { evento, tipo, cantidad });
      onPurchased();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content purchase-modal" onClick={(event) => event.stopPropagation()}>
        <div className="modal-header">
          <div>
            <p className="eyebrow">Comprar entradas</p>
            <h3>{evento.local?.nombreDeEquipo || 'Local'} vs. {evento.visitante?.nombreDeEquipo || 'Visitante'}</h3>
          </div>
          <button className="modal-close" type="button" onClick={onClose} aria-label="Cerrar">x</button>
        </div>

        <p className="purchase-context">{evento.estadio.nombre} · {evento.fecha} · {evento.hora}</p>

        <form onSubmit={handleSubmit}>
          <label>
            Sector
            <select value={tipo} onChange={(event) => setTipo(event.target.value)} required>
              {evento.sectores.map((item) => (
                <option key={item.tipo} value={item.tipo}>
                  {item.tipo} - ${Number(item.precio).toLocaleString('es-UY')}
                </option>
              ))}
            </select>
          </label>

          <label>
            Cantidad
            <input
              type="number"
              min="1"
              max="5"
              value={cantidad}
              onChange={(event) => setCantidad(event.target.value)}
              required
            />
          </label>

          <div className="purchase-total">
            <span>Total estimado con comision</span>
            <strong>${totalEstimado.toLocaleString('es-UY', { maximumFractionDigits: 2 })}</strong>
          </div>

          {error && <p className="error-message">{error}</p>}

          <div className="modal-actions">
            <button className="secondary-button" type="button" onClick={onClose}>Cancelar</button>
            <button className="primary-button" type="submit" disabled={saving || !tipo}>
              {saving ? 'Procesando...' : 'Confirmar compra'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
