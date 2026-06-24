import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { clearSession } from '../services/api';
import * as usuarioService from '../services/usuarioService';
import PurchaseModal from './PurchaseModal';
import TransferModal from './TransferModal';

const TABS = [
  { id: 'eventos', label: 'Eventos' },
  { id: 'entradas', label: 'Mis entradas' },
  { id: 'compras', label: 'Mis compras' },
  { id: 'transferencias', label: 'Transferencias' },
];

export default function UserDashboard({ session, onLogout }) {
  const [activeTab, setActiveTab] = useState('eventos');
  const [eventos, setEventos] = useState([]);
  const [entradas, setEntradas] = useState([]);
  const [compras, setCompras] = useState([]);
  const [transferencias, setTransferencias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [eventoCompra, setEventoCompra] = useState(null);
  const [entradaTransferencia, setEntradaTransferencia] = useState(null);

  const cargarDatos = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const [cartelera, entradasUsuario, comprasUsuario, transferenciasUsuario] = await Promise.all([
        usuarioService.getCartelera(),
        usuarioService.getEntradas(session),
        usuarioService.getCompras(session),
        usuarioService.getTransferenciasRecibidas(session),
      ]);

      setEventos(cartelera);
      setEntradas(entradasUsuario);
      setCompras(comprasUsuario);
      setTransferencias(transferenciasUsuario);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }, [session]);

  useEffect(() => {
    cargarDatos();
  }, [cargarDatos]);

  const transferenciasPendientes = useMemo(
    () => transferencias.filter((item) => item.estadoTransferencia === 'Pendiente').length,
    [transferencias]
  );

  function handleLogout() {
    clearSession();
    onLogout();
  }

  async function handleTransferencia(idTransferencia, accion) {
    setError('');
    setSuccess('');

    try {
      if (accion === 'aceptar') {
        await usuarioService.aceptarTransferencia(idTransferencia);
        setSuccess('Transferencia aceptada. La entrada ya esta disponible en tu cuenta.');
      } else {
        await usuarioService.rechazarTransferencia(idTransferencia);
        setSuccess('Transferencia rechazada.');
      }
      await cargarDatos();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  function compraConfirmada() {
    setEventoCompra(null);
    setSuccess('Compra realizada correctamente.');
    setActiveTab('entradas');
    cargarDatos();
  }

  function transferenciaEnviada() {
    setEntradaTransferencia(null);
    setSuccess('Transferencia enviada. Queda pendiente de aceptacion.');
    cargarDatos();
  }

  return (
    <div className="user-shell">
      <header className="user-header">
        <div>
          <span className="eyebrow">Obligatorio BDII</span>
          <h1>Mis entradas</h1>
        </div>
        <div className="user-header-right">
          <div className="user-identity">
            <strong>{session.correo}</strong>
            <span>{session.paisDoc} {session.tipoDoc} {session.numeroDoc}</span>
          </div>
          <button className="secondary-button" type="button" onClick={handleLogout}>Cerrar sesion</button>
        </div>
      </header>

      <nav className="user-tabs" aria-label="Secciones del usuario">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            className={activeTab === tab.id ? 'user-tab user-tab--active' : 'user-tab'}
            type="button"
            onClick={() => {
              setActiveTab(tab.id);
              setSuccess('');
            }}
          >
            {tab.label}
            {tab.id === 'transferencias' && transferenciasPendientes > 0 ? (
              <span className="tab-count">{transferenciasPendientes}</span>
            ) : null}
          </button>
        ))}
      </nav>

      <main className="user-main">
        <section className="user-summary" aria-label="Resumen de la cuenta">
          <div><span>Entradas actuales</span><strong>{entradas.length}</strong></div>
          <div><span>Compras realizadas</span><strong>{compras.length}</strong></div>
          <div><span>Transferencias pendientes</span><strong>{transferenciasPendientes}</strong></div>
        </section>

        {error && <div className="error-banner user-feedback">{error}</div>}
        {success && <div className="success-banner">{success}</div>}

        {loading ? (
          <p className="loading-text">Cargando tu cuenta...</p>
        ) : (
          <>
            {activeTab === 'eventos' && (
              <EventosView eventos={eventos} onComprar={setEventoCompra} />
            )}
            {activeTab === 'entradas' && (
              <EntradasView entradas={entradas} onTransferir={setEntradaTransferencia} />
            )}
            {activeTab === 'compras' && <ComprasView compras={compras} />}
            {activeTab === 'transferencias' && (
              <TransferenciasView transferencias={transferencias} onAction={handleTransferencia} />
            )}
          </>
        )}
      </main>

      {eventoCompra && (
        <PurchaseModal
          evento={eventoCompra}
          session={session}
          onClose={() => setEventoCompra(null)}
          onPurchased={compraConfirmada}
        />
      )}

      {entradaTransferencia && (
        <TransferModal
          entrada={entradaTransferencia}
          onClose={() => setEntradaTransferencia(null)}
          onTransferred={transferenciaEnviada}
        />
      )}
    </div>
  );
}

function EventosView({ eventos, onComprar }) {
  return (
    <section className="user-section">
      <div className="section-heading">
        <div>
          <p className="eyebrow">Cartelera</p>
          <h2>Eventos disponibles</h2>
        </div>
        <span>{eventos.length} eventos</span>
      </div>

      {eventos.length === 0 ? (
        <p className="empty-text">No hay eventos disponibles.</p>
      ) : (
        <div className="event-grid">
          {eventos.map((evento) => (
            <article className="event-ticket" key={evento.id}>
              <div className="event-date">
                <strong>{formatDay(evento.fecha)}</strong>
                <span>{formatMonth(evento.fecha)}</span>
                <small>{evento.hora?.slice(0, 5)}</small>
              </div>
              <div className="event-info">
                <p>{evento.estadio.nombre}</p>
                <h3>{evento.local?.nombreDeEquipo || 'Equipo local'} vs. {evento.visitante?.nombreDeEquipo || 'Equipo visitante'}</h3>
                <div className="sector-row">
                  {evento.sectores.map((sector) => (
                    <span key={sector.tipo}>{sector.tipo} · ${Number(sector.precio).toLocaleString('es-UY')}</span>
                  ))}
                </div>
              </div>
              <button
                className="primary-button event-buy-button"
                type="button"
                disabled={evento.sectores.length === 0}
                onClick={() => onComprar(evento)}
              >
                Comprar
              </button>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

function EntradasView({ entradas, onTransferir }) {
  return (
    <section className="user-section">
      <div className="section-heading">
        <div><p className="eyebrow">Activos digitales</p><h2>Mis entradas</h2></div>
      </div>

      {entradas.length === 0 ? (
        <p className="empty-text">Todavia no tenes entradas asignadas.</p>
      ) : (
        <div className="ticket-list">
          {entradas.map((entrada) => (
            <article className="owned-ticket" key={entrada.idEntrada}>
              <div className="ticket-number"><span>Entrada</span><strong>#{entrada.idEntrada}</strong></div>
              <div className="ticket-data">
                <div><span>Evento</span><strong>#{entrada.idEvento}</strong></div>
                <div><span>Sector</span><strong>{entrada.tipo}</strong></div>
                <div><span>Estado</span><strong className={`status-text status-text--${entrada.estado.toLowerCase()}`}>{entrada.estado}</strong></div>
                <div><span>Transferencias</span><strong>{entrada.numeroVecesTransferida} / 3</strong></div>
              </div>
              <div className="qr-code-value"><span>QR activo</span><code>{entrada.idQR}</code></div>
              <button
                className="secondary-button"
                type="button"
                disabled={entrada.estado !== 'Activa' || entrada.numeroVecesTransferida >= 3}
                onClick={() => onTransferir(entrada)}
              >
                Transferir
              </button>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

function ComprasView({ compras }) {
  return (
    <section className="user-section">
      <div className="section-heading"><div><p className="eyebrow">Historial</p><h2>Mis compras</h2></div></div>
      {compras.length === 0 ? (
        <p className="empty-text">Todavia no realizaste compras.</p>
      ) : (
        <div className="data-table-wrap">
          <table className="user-table">
            <thead><tr><th>Compra</th><th>Fecha</th><th>Estado</th><th>Comision</th><th>Total</th></tr></thead>
            <tbody>
              {compras.map((compra) => (
                <tr key={compra.id}>
                  <td>#{compra.id}</td><td>{compra.fecha}</td><td>{compra.estado}</td>
                  <td>#{compra.idComision}</td><td className="money-cell">${Number(compra.montoTotal).toLocaleString('es-UY')}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}

function TransferenciasView({ transferencias, onAction }) {
  return (
    <section className="user-section">
      <div className="section-heading"><div><p className="eyebrow">Bandeja</p><h2>Transferencias recibidas</h2></div></div>
      {transferencias.length === 0 ? (
        <p className="empty-text">No tenes transferencias recibidas.</p>
      ) : (
        <div className="transfer-list">
          {transferencias.map((transferencia) => (
            <article className="transfer-row" key={transferencia.idTransferencia}>
              <div><span>Transferencia</span><strong>#{transferencia.idTransferencia}</strong></div>
              <div><span>Entrada</span><strong>#{transferencia.idEntrada}</strong></div>
              <div><span>Fecha</span><strong>{transferencia.fechaTransferencia}</strong></div>
              <div><span>Estado</span><strong>{transferencia.estadoTransferencia}</strong></div>
              {transferencia.estadoTransferencia === 'Pendiente' && (
                <div className="transfer-actions">
                  <button className="secondary-button" type="button" onClick={() => onAction(transferencia.idTransferencia, 'rechazar')}>Rechazar</button>
                  <button className="primary-button" type="button" onClick={() => onAction(transferencia.idTransferencia, 'aceptar')}>Aceptar</button>
                </div>
              )}
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

function formatDay(fecha) {
  return new Date(`${fecha}T00:00:00`).getDate().toString().padStart(2, '0');
}

function formatMonth(fecha) {
  return new Intl.DateTimeFormat('es-UY', { month: 'short' })
    .format(new Date(`${fecha}T00:00:00`))
    .replace('.', '')
    .toUpperCase();
}
