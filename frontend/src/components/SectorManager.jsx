import React, { useEffect, useState } from 'react';
import * as eventoService from '../services/eventoService';

export default function SectorManagerModal({ evento, estadio, onClose }) {
  const [sectoresHabilitados, setSectoresHabilitados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const SECTORES_DISPONIBLES = ['Tribuna', 'Platea', 'Campo'];

  useEffect(() => {
    eventoService.getSectores(evento.id)
      .then(setSectoresHabilitados)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [evento.id]);

  function isHabilitado(tipo) {
    return sectoresHabilitados.some((s) => s.tipo === tipo);
  }

  function getSectorData(tipo) {
    return sectoresHabilitados.find((s) => s.tipo === tipo);
  }

  async function toggleSector(tipo) {
    const habilitado = isHabilitado(tipo);

    try {
      if (habilitado) {
        await eventoService.deshabilitarSector({
          idEvento: evento.id,
          tipo,
        });
      } else {
        const precio = prompt(`Precio para ${tipo}:`, '500');
        const capacidad = prompt(`Capacidad habilitada para ${tipo}:`, '5000');
        if (!precio || !capacidad) return;

        await eventoService.habilitarSector({
          idEvento: evento.id,
          tipo,
          precio: Number(precio),
          capacidadHabilitada: Number(capacidad),
        });
      }

      const updated = await eventoService.getSectores(evento.id);
      setSectoresHabilitados(updated);
    } catch (err) {
      alert('Error: ' + err.message);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Sectores - Evento #{evento.id}</h3>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>

        {error && <p className="error-message">{error}</p>}

        {loading ? (
          <p className="loading-text">Cargando sectores...</p>
        ) : (
          <table className="sectores-table">
            <thead>
              <tr>
                <th>Sector</th>
                <th>Estado</th>
                <th>Precio</th>
                <th>Capacidad</th>
                <th>Accion</th>
              </tr>
            </thead>
            <tbody>
              {SECTORES_DISPONIBLES.map((tipo) => {
                const data = getSectorData(tipo);
                const habilitado = !!data;
                return (
                  <tr key={tipo}>
                    <td><strong>{tipo}</strong></td>
                    <td>
                      <span className={`badge ${habilitado ? 'badge--on' : 'badge--off'}`}>
                        {habilitado ? 'Habilitado' : 'Deshabilitado'}
                      </span>
                    </td>
                    <td>{habilitado ? `$${data.precio}` : '-'}</td>
                    <td>{habilitado ? data.capacidadHabilitada : '-'}</td>
                    <td>
                      <button
                        className={`btn-icon ${habilitado ? 'btn-icon--danger' : 'btn-icon--success'}`}
                        onClick={() => toggleSector(tipo)}
                      >
                        {habilitado ? 'Deshabilitar' : 'Habilitar'}
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}

        <div className="modal-actions">
          <button className="secondary-button" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    </div>
  );
}
