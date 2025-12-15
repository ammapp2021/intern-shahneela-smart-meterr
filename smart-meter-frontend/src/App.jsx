import { useEffect, useState } from 'react';
import React from 'react';
import DetailsPanel from './components/DetailsPanel';
import MeterCard from './components/MeterCard';
import Sidebar from './components/Sidebar';
import './style.css';

function App() {
  const [summary, setSummary] = useState({
    daily: { totalUnits: 0, totalBill: 0 },
    weekly: { totalUnits: 0, totalBill: 0 },
    monthly: { totalUnits: 0, totalBill: 0 },
  });

  const [meters, setMeters] = useState([]);

  useEffect(() => {
    const es = new EventSource('http://localhost:8081/stream/meter-updates');
    es.onmessage = (event) => {
      try {
        const json = event.data.startsWith('data:') ? event.data.slice(5) : event.data;
        const parsed = JSON.parse(json);
        if (parsed?.summary) setSummary(parsed.summary);
        if (parsed?.meters?.daily) setMeters(parsed.meters.daily);
      } catch (e) {
        console.error('Parse error', e);
      }
    };
    es.onerror = () => es.close();
    return () => es.close();
  }, []);

  return (
    <div className="app-root">
      <Sidebar />

      <div className="main-area">
        {/* ===== Dashboard Header ===== */}
        <div className="dashboard-header">
          <h2>⚡ Smart Meter Dashboard</h2>
          <div className="dashboard-info">
            
            <span>Connected</span>
          </div>
        </div>

        <div className="dashboard-container">
          {/* Left Panel - Meter Cards */}
          <div className="left-panel">
            {meters.length === 0 && <p>No live data yet...</p>}
            {meters.map((m) => (
              <MeterCard key={m.id} meter={m} />
            ))}
          </div>

          {/* Right Panel - Details */}
          <div className="right-panel">
            <DetailsPanel data={{ summary, meters: { daily: meters } }} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
