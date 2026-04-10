import { useLocation, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Bell, Settings, User } from 'lucide-react';

const ROUTE_LABELS = {
  '/dashboard':   'Tableau de bord',
  '/organismes':  'Organismes',
  '/employes':    'Employés',
  '/profils':     'Profils',
  '/projets':     'Projets',
  '/phases':      'Phases',
  '/affectations':'Affectations',
  '/livrables':   'Livrables',
  '/documents':   'Documents',
  '/factures':    'Factures',
  '/reporting':   'Reporting',
  '/profil':      'Mon Profil',
};

export default function Topbar() {
  const { pathname } = useLocation();
  const { user } = useAuth();

  const base = '/' + pathname.split('/')[1];
  const label = ROUTE_LABELS[base] || 'SuiviProjet';

  return (
    <header style={{
      position: 'fixed',
      top: 0,
      left: 'var(--sidebar-width)',
      right: 0,
      height: 'var(--topbar-height)',
      background: 'rgba(10,14,26,0.85)',
      backdropFilter: 'blur(12px)',
      borderBottom: '1px solid var(--border)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '0 32px',
      zIndex: 99,
    }}>
      <div style={{ fontFamily: 'var(--font-display)', fontSize: '1.1rem', fontWeight: 700, letterSpacing: '-0.01em' }}>
        {label}
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <Link to="/profil" className="btn btn-secondary btn-icon" title="Mon profil">
          <User size={16} />
        </Link>
      </div>
    </header>
  );
}
