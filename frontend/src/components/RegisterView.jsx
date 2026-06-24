const PAISES_CONO_SUR = [
  { codigo: 'ARG', nombre: 'Argentina' },
  { codigo: 'BRA', nombre: 'Brasil' },
  { codigo: 'CHL', nombre: 'Chile' },
  { codigo: 'PRY', nombre: 'Paraguay' },
  { codigo: 'URY', nombre: 'Uruguay' },
];

import React, { useState } from 'react';

export default function RegisterView({ onNavigate }) {
  const [form, setForm] = useState({
    correo: '',
    password: '',
    paisDoc: 'URY',
    tipoDoc: '',
    numeroDoc: '',
    paisCP: 'URY',
    localidad: '',
    dirCalle: '',
    dirNumero: '',
    codigoPostal: '',
  });
  const [telefonos, setTelefonos] = useState(['']);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  function handlePhoneChange(index, value) {
    setTelefonos((prev) => {
      const updated = [...prev];
      updated[index] = value;
      return updated;
    });
  }

  function addPhone() {
    setTelefonos((prev) => [...prev, '']);
  }

  function removePhone(index) {
    setTelefonos((prev) => prev.filter((_, i) => i !== index));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setStatus('loading');
    setError('');
    setSuccess('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...form,
          telefonos: telefonos.filter((t) => t.trim() !== ''),
        }),
      });

      const text = await response.text();

      if (!response.ok) {
        throw new Error(text || 'Error al registrar');
      }

      setSuccess(text);
      setStatus('success');
    } catch (err) {
      setError(err.message || 'No se pudo registrar');
      setStatus('error');
    }
  }

  if (status === 'success') {
    return (
      <main className="app-shell">
        <section className="login-surface" aria-label="Registro exitoso">
          <div className="brand-panel">
            <p className="eyebrow">Obligatorio BDII</p>
            <h1>Crear cuenta</h1>
            <p>Completá tus datos personales para registrarte en el sistema.</p>
          </div>
          <div className="auth-panel">
            <div className="register-success">
              <div>
                <p className="eyebrow">Listo</p>
                <h2>Registro exitoso</h2>
              </div>
              <p className="success-message">{success}</p>
              <button
                className="primary-button"
                type="button"
                onClick={() => onNavigate('login')}
              >
                Volver al inicio de sesión
              </button>
            </div>
          </div>
        </section>
      </main>
    );
  }

  return (
    <main className="app-shell">
      <section className="login-surface register-surface" aria-label="Registro de usuario">
        <div className="brand-panel">
          <p className="eyebrow">Obligatorio BDII</p>
          <h1>Crear cuenta</h1>
          <p>Completá tus datos personales para registrarte en el sistema.</p>
        </div>

        <div className="auth-panel register-auth-panel">
          <form className="register-form" onSubmit={handleSubmit}>
            <div>
              <p className="eyebrow">Registro</p>
              <h2>Datos personales</h2>
            </div>

            <h3 className="form-section-title">Cuenta</h3>
            <div className="form-grid">
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
                Contraseña
                <input
                  autoComplete="new-password"
                  name="password"
                  onChange={handleChange}
                  required
                  type="password"
                  value={form.password}
                />
              </label>
            </div>

            <h3 className="form-section-title">Documento</h3>
            <div className="form-grid form-grid--3col">
              <label>
                País
                <select name="paisDoc" onChange={handleChange} required value={form.paisDoc}>
                  {PAISES_CONO_SUR.map((p) => (
                    <option key={p.codigo} value={p.codigo}>{p.nombre}</option>
                  ))}
                </select>
              </label>
              <label>
                Tipo
                <select name="tipoDoc" onChange={handleChange} required value={form.tipoDoc}>
                  <option value="CI">Cédula de Identidad</option>
                  <option value="Pasaporte">Pasaporte</option>
                </select>
              </label>
              <label>
                Número
                <input
                  name="numeroDoc"
                  onChange={handleChange}
                  required
                  value={form.numeroDoc}
                />
              </label>
            </div>

            <h3 className="form-section-title">Dirección</h3>
            <div className="form-grid">
              <label>
                País
                <select name="paisCP" onChange={handleChange} required value={form.paisCP}>
                  {PAISES_CONO_SUR.map((p) => (
                    <option key={p.codigo} value={p.codigo}>{p.nombre}</option>
                  ))}
                </select>
              </label>
              <label>
                Localidad
                <input
                  name="localidad"
                  onChange={handleChange}
                  required
                  value={form.localidad}
                />
              </label>
            </div>
            <div className="form-grid form-grid--3col">
              <label>
                Calle
                <input
                  name="dirCalle"
                  onChange={handleChange}
                  required
                  value={form.dirCalle}
                />
              </label>
              <label>
                Número
                <input
                  name="dirNumero"
                  onChange={handleChange}
                  required
                  value={form.dirNumero}
                />
              </label>
              <label>
                Código Postal
                <input
                  name="codigoPostal"
                  onChange={handleChange}
                  required
                  value={form.codigoPostal}
                />
              </label>
            </div>

            <h3 className="form-section-title">Teléfonos de contacto</h3>
            <div className="phone-list">
              {telefonos.map((tel, i) => (
                <div key={i} className="phone-row">
                  <label className="phone-label">
                    Teléfono {i + 1}
                    <input
                      name={`tel-${i}`}
                      onChange={(e) => handlePhoneChange(i, e.target.value)}
                      type="tel"
                      value={tel}
                    />
                  </label>
                  {telefonos.length > 1 && (
                    <button
                      className="btn-icon btn-icon--danger"
                      type="button"
                      onClick={() => removePhone(i)}
                    >
                      X
                    </button>
                  )}
                </div>
              ))}
            </div>
            <button
              className="secondary-button add-phone-btn"
              type="button"
              onClick={addPhone}
            >
              + Agregar teléfono
            </button>

            {error ? <p className="error-message">{error}</p> : null}

            <button
              className="primary-button"
              disabled={status === 'loading'}
              type="submit"
            >
              {status === 'loading' ? 'Registrando...' : 'Registrarse'}
            </button>

            <p className="register-link">
              ¿Ya tenés cuenta?{' '}
              <button
                type="button"
                className="link-button"
                onClick={() => onNavigate('login')}
              >
                Iniciar sesión
              </button>
            </p>
          </form>
        </div>
      </section>
    </main>
  );
}
