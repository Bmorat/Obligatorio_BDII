import React, { useEffect, useState, useCallback } from 'react';
import * as eventoService from '../services/eventoService';
import EventFormModal from './EventForm';
import SectorManagerModal from './SectorManager';

export default function StadiumEvents({ estadio, session }) {
  const [eventos, setEventos] = useState([]);
  const [equiposPorEvento, setEquiposPorEvento] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(null);
  const [showSectores, setShowSectores] = useState(null);

  const cargarEventos = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const eventosData = await eventoService.getEventosPorEstadio(estadio.id);
      setEventos(eventosData);

      const equiposMap = {};
      await Promise.all(
        eventosData.map(async (ev) => {
          try {
            const equipos = await eventoService.getEquiposPorEvento(ev.id);
            const local = equipos.find((eq) => eq.rol === 'Local');
            const visitante = equipos.find((eq) => eq.rol === 'Visitante');
            equiposMap[ev.id] = { local, visitante };
          } catch {
            equiposMap[ev.id] = null;
          }
        })
      );
      setEquiposPorEvento(equiposMap);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [estadio.id]);

  useEffect(() => {
    cargarEventos();
  }, [cargarEventos]);

  async function handleEliminar(id) {
    if (!confirm('¿Eliminar este evento?')) return;
    try {
      await eventoService.eliminarEvento(id);
      cargarEventos();
    } catch (err) {
      alert('Error al eliminar: ' + err.message);
    }
  }

  return (
    <section className="events-section">
      <div className="events-header">
        <h3>Eventos en {estadio.nombre}</h3>
        <button
          className="primary-button"
          onClick={() => setShowForm({ modo: 'crear' })}
        >
          + Crear Evento
        </button>
      </div>

      {error && <div className="error-banner">{error}</div>}

      {loading ? (
        <p className="loading-text">Cargando eventos...</p>
      ) : eventos.length === 0 ? (
        <p className="empty-text">No hay eventos para este estadio.</p>
      ) : (
        <table className="events-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Fecha</th>
              <th>Hora</th>
              <th>Local</th>
              <th>Visitante</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {eventos.map((ev) => {
              const eq = equiposPorEvento[ev.id];
              return (
                <tr key={ev.id}>
                  <td>{ev.id}</td>
                  <td>{ev.fecha}</td>
                  <td>{ev.hora}</td>
                  <td className="team-cell">{eq?.local?.nombreDeEquipo || '-'}</td>
                  <td className="team-cell">{eq?.visitante?.nombreDeEquipo || '-'}</td>
                  <td className="actions-cell">
                    <button
                      className="btn-icon"
                      title="Editar"
                      onClick={() => setShowForm({ modo: 'editar', evento: ev })}
                    >
                      Editar
                    </button>
                    <button
                      className="btn-icon btn-icon--danger"
                      title="Eliminar"
                      onClick={() => handleEliminar(ev.id)}
                    >
                      Eliminar
                    </button>
                    <button
                      className="btn-icon"
                      title="Sectores"
                      onClick={() => setShowSectores(ev)}
                    >
                      Sectores
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {showForm && (
        <EventFormModal
          modo={showForm.modo}
          evento={showForm.evento}
          estadio={estadio}
          session={session}
          onClose={() => setShowForm(null)}
          onSaved={() => {
            setShowForm(null);
            cargarEventos();
          }}
        />
      )}

      {showSectores && (
        <SectorManagerModal
          evento={showSectores}
          estadio={estadio}
          onClose={() => setShowSectores(null)}
        />
      )}
    </section>
  );
}
