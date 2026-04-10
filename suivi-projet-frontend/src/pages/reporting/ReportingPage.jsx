import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { reportingService, projetService } from '../../services/services';
import toast from 'react-hot-toast';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';
import { AlertCircle, Clock, CheckCircle, FolderKanban, TrendingUp, BarChart3, Download } from 'lucide-react';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#f43f5e', '#8b5cf6', '#06b6d4'];

const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload?.length) {
    return (
      <div style={{ background: 'var(--bg-card)', border: '1px solid var(--border)', borderRadius: 8, padding: '10px 14px', fontSize: '0.8rem' }}>
        <p style={{ color: 'var(--text-secondary)', marginBottom: 4 }}>{label}</p>
        {payload.map((p, i) => (
          <p key={i} style={{ color: p.color || 'var(--accent-blue)', fontWeight: 600 }}>
            {p.name}: {Number(p.value).toLocaleString('fr-FR')} MAD
          </p>
        ))}
      </div>
    );
  }
  return null;
};

const TABS = [
  { key: 'overview',          label: 'Vue globale',           icon: BarChart3 },
  { key: 'terminees',         label: 'Non facturées',         icon: AlertCircle },
  { key: 'fact-non-payees',   label: 'Non payées',            icon: Clock },
  { key: 'projets',           label: 'Projets',               icon: FolderKanban },
];

export default function ReportingPage() {
  const [tab, setTab]               = useState('overview');
  const [terminees, setTerminees]   = useState([]);
  const [nonPayees, setNonPayees]   = useState([]);
  const [projets, setProjets]       = useState([]);
  const [factures, setFactures]     = useState([]);
  const [loading, setLoading]       = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const [tRes, npRes, pRes, fRes] = await Promise.all([
          reportingService.termineesNonFacturees(),
          reportingService.factureesNonPayees(),
          reportingService.allProjets(),
          reportingService.allFactures(),
        ]);
        setTerminees(tRes.data);
        setNonPayees(npRes.data);
        setProjets(pRes.data);
        setFactures(fRes.data);
      } catch { toast.error('Erreur de chargement'); }
      finally { setLoading(false); }
    };
    load();
  }, []);

  if (loading) return <div className="loading-center"><div className="spinner" /></div>;

  const totalBudget         = projets.reduce((s, p) => s + p.montant, 0);
  const totalNonFacture     = terminees.reduce((s, p) => s + p.montant, 0);
  const totalNonPaye        = nonPayees.reduce((s, p) => s + p.montant, 0);
  const totalFacture        = factures.reduce((s, f) => s + (f.phase?.montant || 0), 0);
  const projetsEnCours      = projets.filter(p => !p.description?.includes('CLÔTURÉ'));
  const projetsClotures     = projets.filter(p => p.description?.includes('CLÔTURÉ'));

  // Bar chart data: budget par projet (top 8)
  const budgetChartData = projets.slice(0, 8).map(p => ({
    name: p.nom?.split(' ').slice(0, 2).join(' '),
    budget: p.montant,
  }));

  // Pie data: état des projets
  const pieData = [
    { name: 'En cours',  value: projetsEnCours.length },
    { name: 'Clôturés',  value: projetsClotures.length },
  ];

  // Phases pie
  const phasesPie = [
    { name: 'Non facturées', value: terminees.length },
    { name: 'Non payées',    value: nonPayees.length },
    { name: 'Réglées',       value: factures.filter(f => f.phase?.etatPaiement).length },
  ];

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Reporting</h1>
          <p className="page-subtitle">Tableau de bord financier et suivi des projets</p>
        </div>
        <button className="btn btn-secondary" onClick={() => toast.promise(new Promise(r => setTimeout(r, 1500)), { loading: 'Génération du PDF...', success: 'Rapport exporté !', error: 'Erreur' })} style={{ gap: 8 }}>
          <Download size={16} /> Exporter PDF
        </button>
      </div>

      {/* KPI row */}
      <div className="grid-4 mb-6">
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(59,130,246,0.15)' }}>
            <TrendingUp size={20} color="var(--accent-blue)" />
          </div>
          <div>
            <div className="stat-label">Budget total projets</div>
            <div className="stat-value" style={{ fontSize: '1.15rem' }}>{(totalBudget/1000).toFixed(0)}k MAD</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(16,185,129,0.15)' }}>
            <CheckCircle size={20} color="var(--accent-emerald)" />
          </div>
          <div>
            <div className="stat-label">Total facturé</div>
            <div className="stat-value" style={{ fontSize: '1.15rem' }}>{(totalFacture/1000).toFixed(0)}k MAD</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(245,158,11,0.15)' }}>
            <AlertCircle size={20} color="var(--accent-amber)" />
          </div>
          <div>
            <div className="stat-label">À facturer</div>
            <div className="stat-value" style={{ fontSize: '1.15rem' }}>{(totalNonFacture/1000).toFixed(0)}k MAD</div>
            <div className="text-xs text-muted">{terminees.length} phase(s)</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(244,63,94,0.15)' }}>
            <Clock size={20} color="var(--accent-rose)" />
          </div>
          <div>
            <div className="stat-label">En attente paiement</div>
            <div className="stat-value" style={{ fontSize: '1.15rem' }}>{(totalNonPaye/1000).toFixed(0)}k MAD</div>
            <div className="text-xs text-muted">{nonPayees.length} facture(s)</div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div style={{ display: 'flex', gap: 4, marginBottom: 24, background: 'var(--bg-secondary)', padding: 4, borderRadius: 10, width: 'fit-content', flexWrap: 'wrap' }}>
        {TABS.map(t => (
          <button key={t.key} onClick={() => setTab(t.key)} style={{
            display: 'flex', alignItems: 'center', gap: 6,
            padding: '7px 16px', borderRadius: 8, border: 'none', cursor: 'pointer',
            fontSize: '0.85rem', fontWeight: tab === t.key ? 600 : 400,
            background: tab === t.key ? 'var(--bg-card)' : 'transparent',
            color: tab === t.key ? 'var(--text-primary)' : 'var(--text-muted)',
            transition: 'all 0.15s',
          }}>
            <t.icon size={14} />{t.label}
          </button>
        ))}
      </div>

      {/* ── OVERVIEW ── */}
      {tab === 'overview' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
          <div className="grid-2">
            <div className="card">
              <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '0.95rem', fontWeight: 700, marginBottom: 20 }}>
                Budget par projet
              </h3>
              <ResponsiveContainer width="100%" height={240}>
                <BarChart data={budgetChartData} margin={{ left: -20 }}>
                  <XAxis dataKey="name" tick={{ fill: 'var(--text-muted)', fontSize: 11 }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fill: 'var(--text-muted)', fontSize: 11 }} axisLine={false} tickLine={false} />
                  <Tooltip content={<CustomTooltip />} />
                  <Bar dataKey="budget" name="Budget" fill="var(--accent-blue)" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="card">
              <h3 style={{ fontFamily: 'var(--font-display)', fontSize: '0.95rem', fontWeight: 700, marginBottom: 20 }}>
                État des phases
              </h3>
              <ResponsiveContainer width="100%" height={240}>
                <PieChart>
                  <Pie data={phasesPie} cx="50%" cy="50%" innerRadius={60} outerRadius={90} dataKey="value" paddingAngle={3}>
                    {phasesPie.map((_, i) => <Cell key={i} fill={COLORS[i]} />)}
                  </Pie>
                  <Tooltip />
                  <Legend wrapperStyle={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }} />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>
      )}

      {/* ── TERMINEES NON FACTUREES ── */}
      {tab === 'terminees' && (
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 16 }}>
            <AlertCircle size={18} color="var(--accent-amber)" />
            <span style={{ fontWeight: 600, color: 'var(--accent-amber)' }}>
              {terminees.length} phase(s) terminée(s) non facturée(s) — {totalNonFacture.toLocaleString('fr-FR')} MAD à facturer
            </span>
          </div>
          {terminees.length === 0 ? (
            <div className="empty-state card"><CheckCircle size={36} /><p>Toutes les phases réalisées sont facturées !</p></div>
          ) : (
            <div className="card" style={{ padding: 0 }}>
              <div className="table-wrapper">
                <table>
                  <thead><tr><th>Code</th><th>Libellé</th><th>Montant</th><th>Période</th><th>Action</th></tr></thead>
                  <tbody>
                    {terminees.map(p => (
                      <tr key={p.id}>
                        <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                        <td style={{ fontWeight: 500 }}>{p.libelle}</td>
                        <td style={{ fontWeight: 600, color: 'var(--accent-amber)' }}>{p.montant?.toLocaleString('fr-FR')} MAD</td>
                        <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{p.dateDebut} → {p.dateFin}</td>
                        <td>
                          <Link to="/factures" className="btn btn-warning btn-sm">Créer facture</Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}

      {/* ── FACTUREES NON PAYEES ── */}
      {tab === 'fact-non-payees' && (
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 16 }}>
            <Clock size={18} color="var(--accent-rose)" />
            <span style={{ fontWeight: 600, color: 'var(--accent-rose)' }}>
              {nonPayees.length} facture(s) en attente de paiement — {totalNonPaye.toLocaleString('fr-FR')} MAD
            </span>
          </div>
          {nonPayees.length === 0 ? (
            <div className="empty-state card"><CheckCircle size={36} /><p>Toutes les factures sont payées !</p></div>
          ) : (
            <div className="card" style={{ padding: 0 }}>
              <div className="table-wrapper">
                <table>
                  <thead><tr><th>Code</th><th>Libellé</th><th>Montant</th><th>Période</th><th>Facturée</th></tr></thead>
                  <tbody>
                    {nonPayees.map(p => (
                      <tr key={p.id}>
                        <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                        <td style={{ fontWeight: 500 }}>{p.libelle}</td>
                        <td style={{ fontWeight: 600, color: 'var(--accent-rose)' }}>{p.montant?.toLocaleString('fr-FR')} MAD</td>
                        <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{p.dateDebut} → {p.dateFin}</td>
                        <td><span className="badge badge-blue">✓ Facturée</span></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}

      {/* ── PROJETS ── */}
      {tab === 'projets' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {/* En cours */}
          <div>
            <div style={{ fontFamily: 'var(--font-display)', fontSize: '1rem', fontWeight: 700, marginBottom: 12, color: 'var(--accent-emerald)' }}>
              Projets en cours ({projetsEnCours.length})
            </div>
            {projetsEnCours.length === 0 ? (
              <p className="text-muted text-sm">Aucun projet en cours</p>
            ) : (
              <div className="card" style={{ padding: 0 }}>
                <div className="table-wrapper">
                  <table>
                    <thead><tr><th>Code</th><th>Nom</th><th>Chef</th><th>Budget</th><th>Période</th></tr></thead>
                    <tbody>
                      {projetsEnCours.map(p => (
                        <tr key={p.id}>
                          <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                          <td><Link to={`/projets/${p.id}`} style={{ color: 'var(--accent-blue-light)', textDecoration: 'none', fontWeight: 500 }}>{p.nom}</Link></td>
                          <td>{p.chefProjet?.prenom} {p.chefProjet?.nom}</td>
                          <td style={{ fontWeight: 600 }}>{p.montant?.toLocaleString('fr-FR')} MAD</td>
                          <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{p.dateDebut} → {p.dateFin}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
          </div>

          {/* Clôturés */}
          <div>
            <div style={{ fontFamily: 'var(--font-display)', fontSize: '1rem', fontWeight: 700, marginBottom: 12, color: 'var(--text-muted)' }}>
              Projets clôturés ({projetsClotures.length})
            </div>
            {projetsClotures.length === 0 ? (
              <p className="text-muted text-sm">Aucun projet clôturé</p>
            ) : (
              <div className="card" style={{ padding: 0 }}>
                <div className="table-wrapper">
                  <table>
                    <thead><tr><th>Code</th><th>Nom</th><th>Chef</th><th>Budget</th></tr></thead>
                    <tbody>
                      {projetsClotures.map(p => (
                        <tr key={p.id}>
                          <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                          <td><Link to={`/projets/${p.id}`} style={{ color: 'var(--text-secondary)', textDecoration: 'none' }}>{p.nom}</Link></td>
                          <td>{p.chefProjet?.prenom} {p.chefProjet?.nom}</td>
                          <td style={{ fontWeight: 600 }}>{p.montant?.toLocaleString('fr-FR')} MAD</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
