import React from 'react';

export default function MeterCard({ meter }) {
  return (
    <div className="meter-card">
      <div className="card-head">
        <div>Meter ID: <strong>{meter.meterId}</strong></div>
        <div className="active">● Active {meter.status}</div>
      </div>

      <div className="latest-row">
        <div className="latest-label">
          Today units
          <br />
          <span className="time">{new Date().toLocaleTimeString()}</span>
        </div>
        <div className="latest-value">{meter.latest}</div>
      </div>

      <div className="metrics">
        <div>
          <div className="muted">Daily units</div>
          <div className="metric-val">{meter.totalUnits}</div>
        </div>
        <div>
          <div className="muted">Daily bill</div>
          <div className="metric-val">{meter.totalBill}</div>
        </div>
        <div>
          <div className="muted">Date</div>
          <div className="metric-val">{meter.date}</div>
        </div>
      </div>
    </div>
  );
}
