import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser]       = useState(null);
  const [token, setToken]     = useState(() => sessionStorage.getItem('jwt'));
  const [loading, setLoading] = useState(true);

  // Fetch /api/auth/me when token is available
  const fetchMe = useCallback(async (tok) => {
    try {
      api.defaults.headers.common['Authorization'] = `Bearer ${tok}`;
      const res = await api.get('/auth/me');
      setUser(res.data);
    } catch {
      logout();
    } finally {
      setLoading(false);
    }
  }, []); // eslint-disable-line

  useEffect(() => {
    if (token) {
      fetchMe(token);
    } else {
      setLoading(false);
    }
  }, [token, fetchMe]);

  const login = async (username, password) => {
    const res = await api.post('/auth/login', { username, password });
    const tok = res.data.token;
    
    // On met à jour le token avant de fetched 'me'
    sessionStorage.setItem('jwt', tok);
    setToken(tok);
    api.defaults.headers.common['Authorization'] = `Bearer ${tok}`;

    try {
      const me = await api.get('/auth/me');
      setUser(me.data);
      return me.data;
    } catch (err) {
      // Si on arrive pas à récupérer le profil, on nettoie pour éviter un état incohérent
      logout();
      throw new Error("Authentifié, mais impossible de charger votre profil utilisateur.");
    }
  };

  const logout = () => {
    sessionStorage.removeItem('jwt');
    setToken(null);
    setUser(null);
    delete api.defaults.headers.common['Authorization'];
  };

  // Helper: extract role from profil.libelle  e.g. "CHEF_PROJET"
  const role = user?.profil?.libelle?.toUpperCase().replace(/ /g, '_') || null;

  const hasRole = (...roles) => roles.includes(role);

  return (
    <AuthContext.Provider value={{ user, token, login, logout, loading, role, hasRole }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
