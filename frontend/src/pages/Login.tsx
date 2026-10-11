import React from 'react';
import { useNavigate } from 'react-router-dom';
import { RouteIcon } from 'lucide-react';

const Login = () => {
  const navigate = useNavigate();

  const handleDemoLogin = (e: React.FormEvent) => {
    e.preventDefault();
    localStorage.setItem('token', 'demo-token');
    navigate('/dashboard');
  };

  return (
    <div style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center', background: 'radial-gradient(circle at center, #1f2833 0%, #0b0c10 100%)' }}>
      <div className="glass-panel" style={{ padding: '40px', width: '100%', maxWidth: '400px', textAlign: 'center' }}>
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '20px' }}>
          <div style={{ background: 'var(--accent-color)', padding: '12px', borderRadius: '50%', color: '#000' }}>
            <RouteIcon size={32} />
          </div>
        </div>
        <h1 style={{ marginBottom: '8px', fontSize: '24px', fontWeight: 600 }}>RouteIQ</h1>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '32px' }}>Smart Route Optimization System</p>
        
        <form onSubmit={handleDemoLogin}>
          <div className="input-group">
            <input type="email" placeholder="Email Address" className="input-field" defaultValue="demo@routeiq.com" required />
          </div>
          <div className="input-group" style={{ marginBottom: '32px' }}>
            <input type="password" placeholder="Password" className="input-field" defaultValue="password" required />
          </div>
          <button type="submit" className="btn" style={{ width: '100%' }}>
            Sign In
          </button>
        </form>
        
        <div style={{ marginTop: '24px', fontSize: '14px', color: 'var(--text-secondary)' }}>
          For Demo, just click Sign In.
        </div>
      </div>
    </div>
  );
};

export default Login;
