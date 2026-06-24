import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { getSession } from './services/api';
import LoginView from './components/LoginView';
import AdminDashboard from './components/AdminDashboard';
import FuncionarioDashboard from './components/FuncionarioDashboard';
import './styles.css';

function App() {
  const [session, setSession] = useState(null);

  useEffect(() => {
    setSession(getSession());
  }, []);

  if (!session) {
    return <LoginView onLogin={setSession} />;
  }

  if (session.rol === 'ROLE_ADMIN_SEDE') {
    return <AdminDashboard session={session} onLogout={() => setSession(null)} />;
  }

  if (session.rol === 'ROLE_FUNCIONARIO') {
    return <FuncionarioDashboard session={session} onLogout={() => setSession(null)} />;
  }

  return (
    <main className="app-shell">
      <section className="login-surface" aria-label="Sesion activa">
        <div className="brand-panel">
          <p className="eyebrow">Obligatorio BDII</p>
          <h1>Acceso al sistema</h1>
        </div>

        <div className="auth-panel">
          <section className="session-view">
            <div>
              <p className="eyebrow">Sesion activa</p>
              <h2>{session.correo}</h2>
            </div>

            <dl className="session-details">
              <div>
                <dt>Rol</dt>
                <dd>{session.rol.replace('ROLE_', '').replaceAll('_', ' ')}</dd>
              </div>
              <div>
                <dt>Documento</dt>
                <dd>{session.paisDoc} {session.tipoDoc} {session.numeroDoc}</dd>
              </div>
              <div>
                <dt>Token</dt>
                <dd className="token-preview">{session.token}</dd>
              </div>
            </dl>

            <button
              className="secondary-button"
              type="button"
              onClick={() => {
                localStorage.removeItem('bdii.auth.session');
                setSession(null);
              }}
            >
              Cerrar sesion
            </button>
          </section>
        </div>
      </section>
    </main>
  );
}

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
