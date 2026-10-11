import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import type { ReactNode } from 'react';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import './index.css';

// A simple mock auth provider setup
const PrivateRoute = ({ children }: { children: ReactNode }) => {
  // Let's just always render for development until Login is connected
  return children;
};

function App() {
  return (
    <Router>
      <div className="app-container">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route 
            path="/dashboard" 
            element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            } 
          />
          <Route path="/" element={<Navigate to="/dashboard" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
