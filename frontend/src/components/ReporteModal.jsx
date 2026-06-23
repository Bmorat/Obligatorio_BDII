import React from 'react';

export default function ReporteModal({ titulo, columnas, data, loading, onClose }) {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content reporte-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{titulo}</h3>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>

        {loading ? (
          <p className="loading-text">Cargando...</p>
        ) : !data || data.length === 0 ? (
          <p className="empty-text">No hay datos disponibles.</p>
        ) : (
          <div className="reporte-table-wrapper">
            <table className="reporte-table">
              <thead>
                <tr>
                  <th>#</th>
                  {columnas.map((col) => (
                    <th key={col.key}>{col.label}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {data.map((row, i) => (
                  <tr key={i}>
                    <td className="reporte-index">{i + 1}</td>
                    {columnas.map((col) => (
                      <td key={col.key}>
                        {col.render ? col.render(row[col.key], row) : String(row[col.key] ?? '')}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <div className="modal-actions">
          <button className="secondary-button" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    </div>
  );
}
