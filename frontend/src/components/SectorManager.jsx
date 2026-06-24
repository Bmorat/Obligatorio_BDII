import React, { useEffect, useState } from 'react';
import * as eventoService from '../services/eventoService';

const SECTORES = ['A', 'B', 'C', 'D'];

const CAPACIDAD_DEFAULTS = { A: 5000, B: 5000, C: 200, D: 100 };

export default function SectorManagerModal({ evento, estadio, onClose }) {
  const [sectoresHabilitados, setSectoresHabilitados] = useState([]);
  const [capacidadesMax, setCapacidadesMax] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([
      eventoService.getSectores(evento.id),
      eventoService.getCapacidadesSectores(estadio.id),
    ])
      .then(([sectores, capacidades]) => {
        setSectoresHabilitados(sectores);
        setCapacidadesMax(Object.fromEntries(capacidades.map((s) => [s.tipo, s.capacidad])));
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [evento.id, estadio]);

  function isHabilitado(tipo) {
    return sectoresHabilitados.some((s) => s.tipo === tipo);
  }

  function getSectorData(tipo) {
    return sectoresHabilitados.find((s) => s.tipo === tipo);
  }

  async function toggleSector(codigo) {
    const habilitado = isHabilitado(codigo);

    try {
      if (habilitado) {
        await eventoService.deshabilitarSector({
          idEvento: evento.id,
          tipo: codigo,
        });
      } else {
        const precio = prompt(`Precio para sector ${codigo}:`, '500');
        const capacidad = prompt(`Capacidad habilitada para sector ${codigo} (max: ${capacidadesMax[codigo]}):`, String(CAPACIDAD_DEFAULTS[codigo]));
        if (!precio || !capacidad) return;

        await eventoService.habilitarSector({
          idEvento: evento.id,
          tipo: codigo,
          precio: Number(precio),
          capacidadHabilitada: Number(capacidad),
        });
      }

      const updated = await eventoService.getSectores(evento.id);
      setSectoresHabilitados(updated);
    } catch (err) {
      try {
        const parsed = JSON.parse(err.message);
        if (parsed.error === 'No se puede deshabilitar: el sector tiene entradas vendidas') {
          if (confirm(`El sector ${codigo} tiene entradas vendidas. ¿Deshabilitar de todas formas? Se eliminaran las entradas asociadas.`)) {
            await eventoService.deshabilitarSectorForzar({ idEvento: evento.id, tipo: codigo });
            const updated = await eventoService.getSectores(evento.id);
            setSectoresHabilitados(updated);
          }
        } else {
          alert(parsed.error);
        }
      } catch {
        alert('Error: ' + err.message);
      }
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
                <th>Capacidad Max</th>
                <th>Accion</th>
              </tr>
            </thead>
            <tbody>
              {SECTORES.map((codigo) => {
                const data = getSectorData(codigo);
                const habilitado = !!data;
                return (
                  <tr key={codigo}>
                    <td><strong>{codigo}</strong></td>
                    <td>
                      <span className={`badge ${habilitado ? 'badge--on' : 'badge--off'}`}>
                        {habilitado ? 'Habilitado' : 'Deshabilitado'}
                      </span>
                    </td>
                    <td>{habilitado ? `$${data.precio}` : '-'}</td>
                    <td>{habilitado ? data.capacidadHabilitada : '-'}</td>
                    <td>{capacidadesMax[codigo] ?? '-'}</td>
                    <td>
                      <button
                        className={`btn-icon ${habilitado ? 'btn-icon--danger' : 'btn-icon--success'}`}
                        onClick={() => toggleSector(codigo)}
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
