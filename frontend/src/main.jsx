import React from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

function App() {
  return (
    <main className="app">
      <section className="panel">
        <p className="eyebrow">Obligatorio BDII</p>
        <h1>Frontend React + Vite</h1>
        <p>Base inicial para conectar con el backend Spring Boot.</p>
      </section>
    </main>
  );
}

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
