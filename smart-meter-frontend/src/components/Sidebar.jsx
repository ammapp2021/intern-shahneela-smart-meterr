import React from 'react';

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="menu-icon">☰</div>
      <nav className="icons">
        <div className="icon">🏠</div>
        <div className="icon">📊</div>
        <div className="icon">🔌</div>
        <div className="icon">📁</div>
      </nav>
      <div className="settings">⚙️</div>
    </aside>
  );
}
