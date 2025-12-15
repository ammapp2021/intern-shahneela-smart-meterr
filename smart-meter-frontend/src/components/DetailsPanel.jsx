import React, { useState } from 'react';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

export default function DetailsPanel({ data }) {
  const [activeTab, setActiveTab] = useState('DAILY');

  if (!data || !data.summary) return <div className="details-card">No data</div>;

  const tabKey = activeTab.toLowerCase();
  let summary = data.summary[tabKey] || {};

  if ((tabKey === 'weekly' || tabKey === 'monthly') && summary.dateRange) {
    summary.dateDisplay = summary.dateRange;
  } else if (summary.date) {
    summary.dateDisplay = summary.date;
  }

  const metersArray = tabKey === 'daily' ? data.meters?.daily || [] : [];

  const chartData = {
    labels: metersArray.map((m) => m.meterId),
    datasets: [
      {
        label: 'Units',
        data: metersArray.map((m) => m.totalUnits),
        borderColor: '#4f46e5',
        backgroundColor: 'rgba(79,70,229,0.25)',
        tension: 0.45,
        fill: true,
        pointRadius: 5,
        pointHoverRadius: 7,
        animation: { delay: (ctx) => ctx.dataIndex * 120 },
      },
      {
        label: 'Bill (PKR)',
        data: metersArray.map((m) => m.totalBill),
        borderColor: '#ef4444',
        backgroundColor: 'rgba(239,68,68,0.25)',
        tension: 0.45,
        fill: true,
        pointRadius: 5,
        pointHoverRadius: 7,
        animation: { delay: (ctx) => ctx.dataIndex * 120 },
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    animation: { duration: 2000, easing: 'easeInOutQuart' },
    plugins: {
      legend: { position: 'top', labels: { boxWidth: 12, font: { size: 11 } } },
      tooltip: { enabled: true, animation: true },
    },
    scales: {
      x: { ticks: { font: { size: 10 } }, grid: { display: false } },
      y: { ticks: { font: { size: 10 } }, grid: { color: '#f1f5f9' } },
    },
  };

  return (
    <div className="details-card">
      {/* Tabs */}
      <div className="tabs">
        {['DAILY', 'WEEKLY', 'MONTHLY'].map((tab) => (
          <div
            key={tab}
            className={`tab ${activeTab === tab ? 'active' : ''}`}
            onClick={() => setActiveTab(tab)}
          >
            {tab}
          </div>
        ))}
      </div>

      {/* Summary */}
      <div className="summary">
        <div className={summary.totalBill > 1000 ? 'alert-bill' : ''}>
          <h3>Total Units</h3>
          <p>{summary.totalUnits ?? 0}</p>
          {summary.dateDisplay && <span className="summary-date">{summary.dateDisplay}</span>}
        </div>

        <div className={summary.totalBill > 1000 ? 'alert-bill' : ''}>
          <h3>Total Bill (PKR)</h3>
          <p>{summary.totalBill ?? 0}</p>
          {summary.totalBill > 900 && <span className="alert-text">⚠ High Bill!</span>}
          {summary.dateDisplay && <span className="summary-date">{summary.dateDisplay}</span>}
        </div>
      </div>

      {/* Chart */}
      {metersArray.length > 0 && (
        <div className="chart-container">
          <Line data={chartData} options={chartOptions} />
        </div>
      )}
    </div>
  );
}
