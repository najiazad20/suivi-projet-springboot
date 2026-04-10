import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Blocks unauthenticated users
export const PrivateRoute = ({ children }) => {
  const { token, loading } = useAuth();
  const location = useLocation();

  if (loading) return (
    <div className="loading-center" style={{ minHeight: '100vh' }}>
      <div className="spinner" />
    </div>
  );

  if (!token) return <Navigate to="/login" state={{ from: location }} replace />;
  return children;
};

// Blocks users without the required role(s)
export const RoleRoute = ({ children, roles }) => {
  const { role, loading } = useAuth();

  if (loading) return (
    <div className="loading-center" style={{ minHeight: '100vh' }}>
      <div className="spinner" />
    </div>
  );

  if (!roles.includes(role)) return <Navigate to="/403" replace />;
  return children;
};
