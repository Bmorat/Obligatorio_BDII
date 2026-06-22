import React, { useEffect, useState } from 'react';
import * as eventoService from '../services/eventoService';

export default function EventFormModal({ modo, evento, estadio, onClose, onSaved }) {
  const [form, setForm] = useState({
    fecha: '',
    hora: '',
    idEstadio: estadio.id,
    idEquipoLocal: '',
    idEquipoVisitante: '',
  });
  const [equipos, setEquipos] = useState([]);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    eventoService.getEquipos()
      .then(setEquipos)
      .catch(() => {});
  }, []);

  useEffect(() => {
    if (modo === 'editar' && evento) {
      setForm({
        fecha: evento.fecha,
        hora: evento.hora,
        idEstadio: evento.idEstadio,
        idEquipoLocal: '',
        idEquipoVisitante: '',
      });
    }
  }, [modo, evento]);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSaving(true);
    setError('');

    try {
      const payload = {
        ...form,
        idEquipoLocal: form.idEquipoLocal ? Number(form.idEquipoLocal) : null,
        idEquipoVisitante: form.idEquipoVisitante ? Number(form.idEquipoVisitante) : null,
      };

      if (modo === 'crear') {
        await eventoService.crearEvento(payload);
      } else {
        await eventoService.actualizarEvento(evento.id, payload);
      }

      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{modo === 'crear' ? 'Crear Evento' : 'Editar Evento'}</h3>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>

        <form onSubmit={handleSubmit}>
          <label>
            Fecha
            <input
              name="fecha"
              type="date"
              value={form.fecha}
              onChange={handleChange}
              required
            />
          </label>

          <label>
            Hora
            <input
              name="hora"
              type="time"
              value={form.hora}
              onChange={handleChange}
              required
            />
          </label>

          <label>
            Equipo Local
            <select
              name="idEquipoLocal"
              value={form.idEquipoLocal}
              onChange={handleChange}
              required={modo === 'crear'}
            >
              <option value="">Seleccionar...</option>
              {equipos.map((eq) => (
                <option key={eq.id} value={eq.id}>{eq.nombreDeEquipo}</option>
              ))}
            </select>
          </label>

          <label>
            Equipo Visitante
            <select
              name="idEquipoVisitante"
              value={form.idEquipoVisitante}
              onChange={handleChange}
              required={modo === 'crear'}
            >
              <option value="">Seleccionar...</option>
              {equipos.map((eq) => (
                <option key={eq.id} value={eq.id}>{eq.nombreDeEquipo}</option>
              ))}
            </select>
          </label>

          {error && <p className="error-message">{error}</p>}

          <div className="modal-actions">
            <button type="button" className="secondary-button" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="primary-button" disabled={saving}>
              {saving ? 'Guardando...' : modo === 'crear' ? 'Crear' : 'Guardar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
