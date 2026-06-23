import React, { useEffect, useState, useCallback } from 'react';
import { clearSession } from '../services/api';
import * as estadioService from '../services/estadioService';
import StadiumEvents from './StadiumEvents';

export default function AdminDashboard({ session, onLogout }) {
  const [todosEstadios, setTodosEstadios] = useState([]);
  const [misEstadios, setMisEstadios] = useState([]);
  const [estadioActivo, setEstadioActivo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const roleLabel = session.rol.replace('ROLE_', '').replaceAll('_', ' ');

  const cargarDatos = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const [todos, mis] = await Promise.all([
        estadioService.getAll(),
        estadioService.getByAdmin(session.paisDoc, session.tipoDoc, session.numeroDoc),
      ]);
      setTodosEstadios(todos);
      setMisEstadios(mis);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [session]);

  useEffect(() => {
    cargarDatos();
  }, [cargarDatos]);

  function handleLogout() {
    clearSession();
    onLogout();
  }

  function seleccionarEstadio(estadio) {
    setEstadioActivo((prev) => (prev?.id === estadio.id ? null : estadio));
  }

  return (
    <div className="admin-shell">
      <header className="admin-header">
        <div>
          <span className="eyebrow">Obligatorio BDII</span>
          <h1>Panel de Administrador de Sede</h1>
        </div>
        <div className="admin-header-right">
          <span className="admin-user">{session.correo}</span>
          <span className="admin-role-badge">{roleLabel}</span>
          <button className="secondary-button" type="button" onClick={handleLogout}>
            Cerrar sesion
          </button>
        </div>
      </header>

      {error && <div className="error-banner">{error}</div>}

      <div className="admin-layout">
        <aside className="admin-sidebar">
          <h2>Todos los Estadios</h2>
          {loading ? (
            <p className="loading-text">Cargando...</p>
          ) : (
            <ul className="stadium-list">
              {todosEstadios.map((e) => (
                <li key={e.id} className="stadium-card stadium-card--readonly">
                  <strong>{e.nombre}</strong>
                  <span className="stadium-meta">{e.ubicacion}</span>
                </li>
              ))}
            </ul>
          )}
        </aside>

        <main className="admin-main">
          <h2>Mis Estadios Asignados</h2>
          {loading ? (
            <p className="loading-text">Cargando...</p>
          ) : misEstadios.length === 0 ? (
            <p className="empty-text">No tienes estadios asignados.</p>
          ) : (
            <>
              <ul className="stadium-list">
                {misEstadios.map((e) => (
                  <li
                    key={e.id}
                    className={`stadium-card stadium-card--assigned${
                      estadioActivo?.id === e.id ? ' stadium-card--active' : ''
                    }`}
                    onClick={() => seleccionarEstadio(e)}
                    role="button"
                    tabIndex={0}
                    onKeyDown={(ev) => {
                      if (ev.key === 'Enter' || ev.key === ' ') seleccionarEstadio(e);
                    }}
                  >
                    <strong>{e.nombre}</strong>
                    <span className="stadium-meta">{e.ubicacion}</span>
                  </li>
                ))}
              </ul>

              {estadioActivo && (
                <StadiumEvents
                  key={estadioActivo.id}
                  estadio={estadioActivo}
                  session={session}
                />
              )}
            </>
          )}
        </main>
      </div>
    </div>
  );
}
