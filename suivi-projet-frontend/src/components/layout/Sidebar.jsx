import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  LayoutDashboard, Building2, Users, FolderKanban,
  GitBranch, UserCheck, FileText, Package, Receipt,
  BarChart3, LogOut, ChevronRight, Briefcase, Shield
} from 'lucide-react';

const MENU = [
  {
    label: 'Vue générale',
    items: [
      { to: '/dashboard', icon: LayoutDashboard, label: 'Tableau de bord',
        roles: ['ADMINISTRATEUR','DIRECTEUR','CHEF_PROJET','COMPTABLE','SECRETAIRE'] },
    ]
  },
  {
    label: 'Organisation',
    items: [
      { to: '/organismes', icon: Building2, label: 'Organismes',
        roles: ['SECRETAIRE','DIRECTEUR','ADMINISTRATEUR'] },
      { to: '/employes', icon: Users, label: 'Employés',
        roles: ['ADMINISTRATEUR','DIRECTEUR','CHEF_PROJET'] },
      { to: '/profils', icon: Shield, label: 'Profils',
        roles: ['ADMINISTRATEUR'] },
    ]
  },
  {
    label: 'Projets',
    items: [
      { to: '/projets', icon: FolderKanban, label: 'Projets',
        roles: ['SECRETAIRE','DIRECTEUR','CHEF_PROJET','ADMINISTRATEUR'] },
      { to: '/phases', icon: GitBranch, label: 'Phases',
        roles: ['CHEF_PROJET','DIRECTEUR','ADMINISTRATEUR'] },
      { to: '/affectations', icon: UserCheck, label: 'Affectations',
        roles: ['CHEF_PROJET','ADMINISTRATEUR'] },
    ]
  },
  {
    label: 'Documents',
    items: [
      { to: '/livrables', icon: Package, label: 'Livrables',
        roles: ['CHEF_PROJET','DIRECTEUR','ADMINISTRATEUR'] },
      { to: '/documents', icon: FileText, label: 'Documents',
        roles: ['CHEF_PROJET','DIRECTEUR','SECRETAIRE','ADMINISTRATEUR'] },
    ]
  },
  {
    label: 'Finance',
    items: [
      { to: '/factures', icon: Receipt, label: 'Factures',
        roles: ['COMPTABLE','ADMINISTRATEUR'] },
      { to: '/reporting', icon: BarChart3, label: 'Reporting',
        roles: ['DIRECTEUR','CHEF_PROJET','COMPTABLE','ADMINISTRATEUR'] },
    ]
  },
];

export default function Sidebar() {
  const { user, logout, hasRole, role } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const roleBadgeColor = {
    'ADMINISTRATEUR': 'badge-rose',
    'DIRECTEUR':      'badge-violet',
    'CHEF_PROJET':    'badge-blue',
    'COMPTABLE':      'badge-amber',
    'SECRETAIRE':     'badge-cyan',
  };

  return (
    <aside style={{
      position: 'fixed',
      left: 0, top: 0, bottom: 0,
      width: 'var(--sidebar-width)',
      background: 'var(--bg-secondary)',
      borderRight: '1px solid var(--border)',
      display: 'flex',
      flexDirection: 'column',
      zIndex: 100,
      overflowY: 'auto',
    }}>
      {/* Logo */}
      <div style={{
        padding: '20px 20px 16px',
        borderBottom: '1px solid var(--border)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <div style={{
            width: 36, height: 36,
            background: 'linear-gradient(135deg, var(--accent-blue), var(--accent-violet))',
            borderRadius: 10,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            boxShadow: '0 0 16px rgba(59,130,246,0.4)',
          }}>
            <Briefcase size={18} color="#fff" />
          </div>
          <div>
            <div style={{ fontFamily: 'var(--font-display)', fontWeight: 700, fontSize: '0.95rem', letterSpacing: '-0.02em' }}>
              SuiviProjet
            </div>
            <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>Gestion de projets</div>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav style={{ flex: 1, padding: '12px 10px' }}>
        {MENU.map(section => {
          const visibleItems = section.items.filter(item => hasRole(...item.roles));
          if (!visibleItems.length) return null;
          return (
            <div key={section.label} style={{ marginBottom: 20 }}>
              <div style={{
                fontSize: '0.65rem',
                fontWeight: 700,
                letterSpacing: '0.1em',
                textTransform: 'uppercase',
                color: 'var(--text-muted)',
                padding: '0 10px',
                marginBottom: 6,
              }}>
                {section.label}
              </div>
              {visibleItems.map(item => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  style={({ isActive }) => ({
                    display: 'flex',
                    alignItems: 'center',
                    gap: 10,
                    padding: '9px 12px',
                    borderRadius: 8,
                    marginBottom: 2,
                    textDecoration: 'none',
                    fontSize: '0.875rem',
                    fontWeight: isActive ? 600 : 400,
                    color: isActive ? 'var(--accent-blue-light)' : 'var(--text-secondary)',
                    background: isActive ? 'rgba(59,130,246,0.12)' : 'transparent',
                    border: isActive ? '1px solid rgba(59,130,246,0.2)' : '1px solid transparent',
                    transition: 'all 0.15s',
                  })}
                >
                  {({ isActive }) => (
                    <>
                      <item.icon size={16} style={{ flexShrink: 0 }} />
                      <span style={{ flex: 1 }}>{item.label}</span>
                      {isActive && <ChevronRight size={13} />}
                    </>
                  )}
                </NavLink>
              ))}
            </div>
          );
        })}
      </nav>

      {/* User info */}
      <div style={{
        padding: '14px 16px',
        borderTop: '1px solid var(--border)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 10 }}>
          <div style={{
            width: 34, height: 34,
            background: 'var(--bg-hover)',
            borderRadius: '50%',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '0.8rem', fontWeight: 700,
            color: 'var(--accent-blue-light)',
            border: '1px solid var(--border)',
            flexShrink: 0,
          }}>
            {user?.prenom?.[0]}{user?.nom?.[0]}
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontSize: '0.85rem', fontWeight: 500, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
              {user?.prenom} {user?.nom}
            </div>
            <span className={`badge badge-sm ${roleBadgeColor[role] || 'badge-gray'}`} style={{ fontSize: '0.65rem', padding: '1px 7px' }}>
              {role?.replace(/_/g, ' ')}
            </span>
          </div>
        </div>
        <button className="btn btn-secondary" style={{ width: '100%', justifyContent: 'center', padding: '7px' }} onClick={handleLogout}>
          <LogOut size={14} />
          Déconnexion
        </button>
      </div>
    </aside>
  );
}
