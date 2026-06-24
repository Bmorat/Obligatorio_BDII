import React, { useEffect, useState, useCallback } from 'react';
import { clearSession } from '../services/api';
import * as estadioService from '../services/estadioService';
import * as reporteService from '../services/reporteService';
import StadiumEvents from './StadiumEvents';
import ReporteModal from './ReporteModal';

export default function AdminDashboard({ session, onLogout }) {
  const [todosEstadios, setTodosEstadios] = useState([]);
  const [misEstadios, setMisEstadios] = useState([]);
  const [estadioActivo, setEstadioActivo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [reporteModal, setReporteModal] = useState(null);
  const [reporteData, setReporteData] = useState(null);
  const [reporteLoading, setReporteLoading] = useState(false);

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

  async function abrirReporte(tipo) {
    setReporteModal(tipo);
    setReporteLoading(true);
    setReporteData(null);
    setError('');
    try {
      if (tipo === 'eventos') {
        setReporteData(await reporteService.getEventosMasVendidos());
      } else {
        setReporteData(await reporteService.getMayoresCompradores());
      }
    } catch (err) {
      setReporteData([]);
      setError(err.message);
    } finally {
      setReporteLoading(false);
    }
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
          <button className="reporte-btn" type="button" onClick={() => abrirReporte('eventos')}>
            Reporte: Eventos mas vendidos
          </button>
          <button className="reporte-btn" type="button" onClick={() => abrirReporte('compradores')}>
            Ranking: Mayores compradores
          </button>
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

      {reporteModal === 'eventos' && (
        <ReporteModal
          titulo="Reporte: Eventos más vendidos"
          columnas={[
            { label: 'ID Evento', key: 'idEvento' },
            { label: 'Fecha', key: 'fecha' },
            { label: 'Hora', key: 'hora' },
            { label: 'Total Entradas', key: 'totalEntradas' },
            {
              label: 'Total Remunerado',
              key: 'totalRemunerado',
              render: (val) => `$${Number(val).toLocaleString('es-UY', { minimumFractionDigits: 2 })}`,
            },
          ]}
          data={reporteData}
          loading={reporteLoading}
          onClose={() => { setReporteModal(null); setError(''); }}
        />
      )}

      {reporteModal === 'compradores' && (
        <ReporteModal
          titulo="Ranking: Mayores compradores"
          columnas={[
            { label: 'Correo', key: 'correo' },
            { label: 'País Doc', key: 'paisDoc' },
            { label: 'Tipo Doc', key: 'tipoDoc' },
            { label: 'Número Doc', key: 'numeroDoc' },
            { label: 'Total Entradas', key: 'totalEntradas' },
            {
              label: 'Total Gastado',
              key: 'totalGastado',
              render: (val) => `$${Number(val).toLocaleString('es-UY', { minimumFractionDigits: 2 })}`,
            },
          ]}
          data={reporteData}
          loading={reporteLoading}
          onClose={() => { setReporteModal(null); setError(''); }}
        />
      )}
    </div>
  );
}
