import React, { useState } from 'react';
import { setSession } from '../services/api';

export default function LoginView({ onLogin, onNavigate }) {
  const [form, setForm] = useState({
    correo: 'maria.garcia@email.com',
    password: 'password123',
  });
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState('');

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setStatus('loading');
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        throw new Error('Credenciales invalidas');
      }

      const data = await response.json();
      setSession(data);
      setStatus('success');
      onLogin(data);
    } catch (requestError) {
      setError(requestError.message || 'No se pudo iniciar sesion');
      setStatus('error');
    }
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

            <p className="register-link">
              ¿No tenés cuenta?{' '}
              <button type="button" className="link-button" onClick={() => onNavigate('register')}>
                Registrate
              </button>
            </p>
          </form>
        </div>
      </section>
    </main>
  );
}
