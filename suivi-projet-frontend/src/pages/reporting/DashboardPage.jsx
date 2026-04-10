import { useState, useEffect } from 'react';
import { projetService, factureService, phaseService, employeService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { Link } from 'react-router-dom';
import {
  FolderKanban, Receipt, GitBranch, Users,
  TrendingUp, AlertCircle, CheckCircle, Clock, Plus, Building2
} from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#f43f5e', '#8b5cf6'];

const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload?.length) {
    return (
      <div style={{ background: 'var(--bg-card)', border: '1px solid var(--border)', borderRadius: 8, padding: '10px 14px', fontSize: '0.8rem' }}>
        <p style={{ color: 'var(--text-secondary)', marginBottom: 4 }}>{label}</p>
        {payload.map((p, i) => (
          <p key={i} style={{ color: p.color, fontWeight: 600 }}>{p.name}: {p.value.toLocaleString('fr-FR')} MAD</p>
        ))}
      </div>
    );
  }
  return null;
};

export default function DashboardPage() {
  const { hasRole, role, user } = useAuth();
  const [projets, setProjets] = useState([]);
  const [factures, setFactures] = useState([]);
  const [terminees, setTerminees] = useState([]);
  const [factNonPayees, setFactNonPayees] = useState([]);
  const [employes, setEmployes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const calls = [];
        if (hasRole('DIRECTEUR','SECRETAIRE','CHEF_PROJET','ADMINISTRATEUR')) {
          calls.push(projetService.getAll().then(r => setProjets(r.data)));
        }
        if (hasRole('COMPTABLE','DIRECTEUR','ADMINISTRATEUR')) {
          calls.push(factureService.getAll().then(r => setFactures(r.data)));
          calls.push(phaseService.termineesNonFacturees().then(r => setTerminees(r.data)));
          calls.push(phaseService.factureesNonPayees().then(r => setFactNonPayees(r.data)));
        }
        if (hasRole('ADMINISTRATEUR','DIRECTEUR','CHEF_PROJET')) {
          calls.push(employeService.getAll().then(r => setEmployes(r.data)));
        }
        await Promise.allSettled(calls);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading-center"><div className="spinner" /></div>;

  const totalMontantProjets = projets.reduce((s, p) => s + p.montant, 0);
  const totalFacture = factures.reduce((s, f) => s + (f.phase?.montant || 0), 0);
  const projetsEnCours = projets.filter(p => !p.description?.includes('CLÔTURÉ')).length;

  // Chart data: projets par montant (top 5)
  const chartData = projets.slice(0, 6).map(p => ({
    name: p.nom?.split(' ').slice(0, 2).join(' '),
    montant: p.montant,
  }));

  const pieData = [
    { name: 'En cours', value: projetsEnCours },
    { name: 'Clôturés', value: projets.length - projetsEnCours },
  ];

  return (
    <div>
      <div className="page-header" style={{ marginBottom: 24, borderBottom: 'none', paddingBottom: 0 }}>
        <div>
          <h1 className="page-title" style={{ fontSize: '1.8rem' }}>Bonjour, {user?.prenom} 👋</h1>
          <p className="page-subtitle">Voici un aperçu de l'activité de vos projets pour aujourd'hui.</p>
        </div>
      </div>

      {/* Stats */}
      <div className="grid-4 mb-6">
        {hasRole('DIRECTEUR','SECRETAIRE','CHEF_PROJET','ADMINISTRATEUR') && (
          <>
            <div className="stat-card">
              <div className="stat-icon" style={{ background: 'rgba(59,130,246,0.15)' }}>
                <FolderKanban size={22} color="var(--accent-blue)" />
              </div>
              <div>
                <div className="stat-label">Projets</div>
                <div className="stat-value">{projets.length}</div>
                <div className="text-muted text-xs">{projetsEnCours} en cours</div>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ background: 'rgba(16,185,129,0.15)' }}>
                <TrendingUp size={22} color="var(--accent-emerald)" />
              </div>
              <div>
                <div className="stat-label">Budget total</div>
                <div className="stat-value" style={{ fontSize: '1.2rem' }}>{(totalMontantProjets/1000).toFixed(0)}k</div>
                <div className="text-muted text-xs">MAD</div>
              </div>
            </div>
          </>
        )}

        {hasRole('COMPTABLE','DIRECTEUR','ADMINISTRATEUR') && (
          <>
            <div className="stat-card">
              <div className="stat-icon" style={{ background: 'rgba(245,158,11,0.15)' }}>
                <AlertCircle size={22} color="var(--accent-amber)" />
              </div>
              <div>
                <div className="stat-label">Non facturées</div>
                <div className="stat-value">{terminees.length}</div>
                <div className="text-muted text-xs">phases terminées</div>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ background: 'rgba(244,63,94,0.15)' }}>
                <Clock size={22} color="var(--accent-rose)" />
              </div>
              <div>
                <div className="stat-label">Non payées</div>
                <div className="stat-value">{factNonPayees.length}</div>
                <div className="text-muted text-xs">factures en attente</div>
              </div>
            </div>
          </>
        )}

        {hasRole('ADMINISTRATEUR','DIRECTEUR','CHEF_PROJET') && (
          <div className="stat-card">
            <div className="stat-icon" style={{ background: 'rgba(139,92,246,0.15)' }}>
              <Users size={22} color="var(--accent-violet)" />
            </div>
            <div>
              <div className="stat-label">Employés</div>
              <div className="stat-value">{employes.length}</div>
              <div className="text-muted text-xs">actifs</div>
            </div>
          </div>
        )}

        {hasRole('COMPTABLE','DIRECTEUR','ADMINISTRATEUR') && (
          <div className="stat-card">
            <div className="stat-icon" style={{ background: 'rgba(6,182,212,0.15)' }}>
              <Receipt size={22} color="var(--accent-cyan)" />
            </div>
            <div>
              <div className="stat-label">Factures</div>
              <div className="stat-value">{factures.length}</div>
              <div className="text-muted text-xs">émises</div>
            </div>
          </div>
        )}
      </div>
 
      {/* Quick Actions */}
      <div className="mb-6">
        <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '0.95rem', fontWeight: 700, marginBottom: 14 }}>Actions rapides</h3>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          {hasRole('ADMINISTRATEUR') && (
            <>
              <Link to="/employes" className="btn btn-secondary" style={{ gap: 8 }}>
                <Users size={16} /> Gérer les employés
              </Link>
              <Link to="/profils" className="btn btn-secondary" style={{ gap: 8 }}>
                <Plus size={16} /> Gérer les profils
              </Link>
            </>
          )}
          {hasRole('SECRETAIRE', 'DIRECTEUR', 'ADMINISTRATEUR') && (
            <>
              <Link to="/projets" className="btn btn-primary" style={{ gap: 8 }}>
                <Plus size={16} /> Nouveau projet
              </Link>
              <Link to="/organismes" className="btn btn-secondary" style={{ gap: 8 }}>
                <Building2 size={16} /> Gérer les organismes
              </Link>
            </>
          )}
          {hasRole('SECRETAIRE', 'CHEF_PROJET', 'ADMINISTRATEUR') && (
            <Link to="/documents" className="btn btn-secondary" style={{ gap: 8 }}>
              <Plus size={16} /> Nouveau document
            </Link>
          )}
          {hasRole('COMPTABLE', 'ADMINISTRATEUR') && (
            <Link to="/factures" className="btn btn-primary" style={{ gap: 8 }}>
              <Receipt size={16} /> Nouvelle facture
            </Link>
          )}
          {hasRole('CHEF_PROJET', 'ADMINISTRATEUR') && (
            <Link to="/phases" className="btn btn-secondary" style={{ gap: 8 }}>
              <GitBranch size={16} /> Suivre les phases
            </Link>
          )}
          <Link to="/reporting" className="btn btn-secondary" style={{ gap: 8 }}>
            <TrendingUp size={16} /> Voir rapports
          </Link>
        </div>
      </div>

      {/* Charts */}
      {hasRole('DIRECTEUR','SECRETAIRE','CHEF_PROJET','ADMINISTRATEUR') && projets.length > 0 && (
        <div className="grid-2 mb-6">
          <div className="card">
            <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '0.95rem', fontWeight: 700, marginBottom: 20 }}>
              Budget par projet
            </h3>
            <ResponsiveContainer width="100%" height={220}>
              <BarChart data={chartData} margin={{ top: 0, right: 0, left: -20, bottom: 0 }}>
                <XAxis dataKey="name" tick={{ fill: 'var(--text-muted)', fontSize: 11 }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fill: 'var(--text-muted)', fontSize: 11 }} axisLine={false} tickLine={false} />
                <Tooltip content={<CustomTooltip />} />
                <Bar dataKey="montant" fill="var(--accent-blue)" radius={[4,4,0,0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="card">
            <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '0.95rem', fontWeight: 700, marginBottom: 20 }}>
              État des projets
            </h3>
            <ResponsiveContainer width="100%" height={220}>
              <PieChart>
                <Pie data={pieData} cx="50%" cy="50%" innerRadius={55} outerRadius={85} dataKey="value" paddingAngle={3}>
                  {pieData.map((_, i) => <Cell key={i} fill={COLORS[i]} />)}
                </Pie>
                <Tooltip />
                <Legend wrapperStyle={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      )}

      {/* Alerts for comptable */}
      {hasRole('COMPTABLE','DIRECTEUR','ADMINISTRATEUR') && terminees.length > 0 && (
        <div className="card" style={{ border: '1px solid rgba(245,158,11,0.3)', background: 'rgba(245,158,11,0.05)', marginBottom: 20 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 12 }}>
            <AlertCircle size={18} color="var(--accent-amber)" />
            <span style={{ fontWeight: 600, color: 'var(--accent-amber)' }}>Phases terminées non facturées ({terminees.length})</span>
            <Link to="/reporting" className="btn btn-warning btn-sm" style={{ marginLeft: 'auto' }}>Voir tout</Link>
          </div>
          {terminees.slice(0, 3).map(p => (
            <div key={p.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--border)', fontSize: '0.85rem' }}>
              <span>{p.libelle}</span>
              <span style={{ color: 'var(--accent-amber)', fontWeight: 600 }}>{p.montant.toLocaleString('fr-FR')} MAD</span>
            </div>
          ))}
        </div>
      )}

      {/* Recent projects */}
      {hasRole('DIRECTEUR','SECRETAIRE','CHEF_PROJET','ADMINISTRATEUR') && projets.length > 0 && (
        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 16 }}>
            <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '0.95rem', fontWeight: 700 }}>Projets récents</h3>
            <Link to="/projets" className="btn btn-secondary btn-sm">Voir tout</Link>
          </div>
          <div className="table-wrapper">
            <table>
              <thead><tr>
                <th>Nom</th><th>Code</th><th>Chef de projet</th><th>Budget</th><th>Dates</th>
              </tr></thead>
              <tbody>
                {projets.slice(0, 5).map(p => (
                  <tr key={p.id}>
                    <td><Link to={`/projets/${p.id}`} style={{ color: 'var(--accent-blue-light)', textDecoration: 'none', fontWeight: 500 }}>{p.nom}</Link></td>
                    <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                    <td>{p.chefProjet?.prenom} {p.chefProjet?.nom}</td>
                    <td style={{ fontWeight: 600 }}>{p.montant?.toLocaleString('fr-FR')} MAD</td>
                    <td style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>{p.dateDebut} → {p.dateFin}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
