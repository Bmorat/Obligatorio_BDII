import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

const API_BASE_URL = 'http://localhost:8080';
const SESSION_STORAGE_KEY = 'bdii.auth.session';

function App() {
  const [form, setForm] = useState({
    correo: 'maria.garcia@email.com',
    password: 'password123',
  });
  const [session, setSession] = useState(null);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState('');

  useEffect(() => {
    const storedSession = localStorage.getItem(SESSION_STORAGE_KEY);

    if (storedSession) {
      try {
        setSession(JSON.parse(storedSession));
      } catch {
        localStorage.removeItem(SESSION_STORAGE_KEY);
      }
    }
  }, []);

  const roleLabel = useMemo(() => {
    if (!session?.rol) {
      return '';
    }

    return session.rol.replace('ROLE_', '').replaceAll('_', ' ');
  }, [session]);

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setStatus('loading');
    setError('');

    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        throw new Error('Credenciales invalidas');
      }

      const data = await response.json();
      localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(data));
      setSession(data);
      setStatus('success');
    } catch (requestError) {
      setError(requestError.message || 'No se pudo iniciar sesion');
      setStatus('error');
    }
  }

  function handleLogout() {
    localStorage.removeItem(SESSION_STORAGE_KEY);
    setSession(null);
    setStatus('idle');
    setError('');
  }

  return (
    <main className="app-shell">
      <section className="login-surface" aria-label="Inicio de sesion">
        <div className="brand-panel">
          <p className="eyebrow">Obligatorio BDII</p>
          <h1>Acceso al sistema</h1>
          <p>
            Inicia sesion con un usuario cargado en la base para obtener el token
            y trabajar con los endpoints protegidos.
          </p>
        </div>

        <div className="auth-panel">
          {session ? (
            <section className="session-view" aria-label="Sesion activa">
              <div>
                <p className="eyebrow">Sesion activa</p>
                <h2>{session.correo}</h2>
              </div>

              <dl className="session-details">
                <div>
                  <dt>Rol</dt>
                  <dd>{roleLabel}</dd>
                </div>
                <div>
                  <dt>Documento</dt>
                  <dd>
                    {session.paisDoc} {session.tipoDoc} {session.numeroDoc}
                  </dd>
                </div>
                <div>
                  <dt>Token</dt>
                  <dd className="token-preview">{session.token}</dd>
                </div>
              </dl>

              <button className="secondary-button" type="button" onClick={handleLogout}>
                Cerrar sesion
              </button>
            </section>
          ) : (
            <form className="login-form" onSubmit={handleSubmit}>
              <div>
                <p className="eyebrow">Login</p>
                <h2>Entrar</h2>
              </div>

              <label>
                Correo
                <input
                  autoComplete="email"
                  name="correo"
                  onChange={handleChange}
                  required
                  type="email"
                  value={form.correo}
                />
              </label>

              <label>
                Password
                <input
                  autoComplete="current-password"
                  name="password"
                  onChange={handleChange}
                  required
                  type="password"
                  value={form.password}
                />
              </label>

              {error ? <p className="error-message">{error}</p> : null}

              <button className="primary-button" disabled={status === 'loading'} type="submit">
                {status === 'loading' ? 'Ingresando...' : 'Ingresar'}
              </button>
            </form>
          )}
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
