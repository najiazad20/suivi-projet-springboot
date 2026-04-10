import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projetService, phaseService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Search, GitBranch, CheckCircle, Circle, ChevronRight, Filter, Pencil, Trash2, Receipt, DollarSign } from 'lucide-react';

function PhaseForm({ initial, projetId, onSave, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial ? {
      ...initial,
      dateDebut: initial.dateDebut?.substring(0, 10),
      dateFin: initial.dateFin?.substring(0, 10),
      projetId,
    } : { projetId }
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        <div className="form-group">
          <label className="form-label">Code</label>
          <input className="form-control" placeholder="PH-2026-001"
            {...register('code', { required: 'Requis' })} />
          {errors.code && <span className="form-error">{errors.code.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Libellé</label>
          <input className="form-control" placeholder="Développement Backend"
            {...register('libelle', { required: 'Requis' })} />
          {errors.libelle && <span className="form-error">{errors.libelle.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Date début</label>
          <input className="form-control" type="date"
            {...register('dateDebut', { required: 'Requis' })} />
          {errors.dateDebut && <span className="form-error">{errors.dateDebut.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Date fin</label>
          <input className="form-control" type="date"
            {...register('dateFin', { required: 'Requis' })} />
          {errors.dateFin && <span className="form-error">{errors.dateFin.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Montant (MAD)</label>
          <input className="form-control" type="number" step="0.01"
            {...register('montant', { required: 'Requis', valueAsNumber: true })} />
          {errors.montant && <span className="form-error">{errors.montant.message}</span>}
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">Description</label>
          <textarea className="form-control" rows={2}
            {...register('description')} />
        </div>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary">Enregistrer</button>
      </div>
    </form>
  );
}

export default function PhasesPage() {
  const { hasRole } = useAuth();
  const [projets, setProjets]   = useState([]);
  const [phases, setPhases]     = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [search, setSearch]     = useState('');
  const [filterProjet, setFilterProjet] = useState('');
  const [filterEtat, setFilterEtat]     = useState('');
  const [modal, setModal]       = useState(null);
  const [selected, setSelected] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const projRes = await projetService.getAll();
      setProjets(projRes.data);
      // Load all phases for all projects
      const allPhases = await Promise.all(
        projRes.data.map(p =>
          phaseService.getByProjet(p.id)
            .then(r => r.data.map(ph => ({ ...ph, projetNom: p.nom, projetId: p.id })))
            .catch(() => [])
        )
      );
      const flat = allPhases.flat();
      setPhases(flat);
      setFiltered(flat);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    let result = phases;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter(p =>
        p.libelle?.toLowerCase().includes(q) ||
        p.code?.toLowerCase().includes(q) ||
        p.projetNom?.toLowerCase().includes(q)
      );
    }
    if (filterProjet) result = result.filter(p => String(p.projetId) === filterProjet);
    if (filterEtat === 'terminee')     result = result.filter(p => p.etatRealisation);
    if (filterEtat === 'non-terminee') result = result.filter(p => !p.etatRealisation);
    if (filterEtat === 'facturee')     result = result.filter(p => p.etatFacturation);
    if (filterEtat === 'payee')        result = result.filter(p => p.etatPaiement);
    setFiltered(result);
  }, [search, filterProjet, filterEtat, phases]);

  const handleUpdate = async (data) => {
    try {
      await phaseService.update(selected.id, { ...data, montant: Number(data.montant) });
      toast.success('Phase mise à jour');
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      if (!deleteId) return;
      await phaseService.delete(deleteId);
      toast.success('Phase supprimée');
      setDeleteId(null);
      load();
    } catch { toast.error('Erreur'); }
  };

  const toggleState = async (phase, type) => {
    try {
      if (type === 'realisation') await phaseService.setRealisation(phase.id);
      if (type === 'facturation') await phaseService.setFacturation(phase.id);
      if (type === 'paiement')    await phaseService.setPaiement(phase.id);
      toast.success('État mis à jour');
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const getStatusBadge = (phase) => {
    if (phase.etatPaiement)    return <span className="badge badge-amber">Payée</span>;
    if (phase.etatFacturation) return <span className="badge badge-blue">Facturée</span>;
    if (phase.etatRealisation) return <span className="badge badge-green">Réalisée</span>;
    return <span className="badge badge-gray">En cours</span>;
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Phases</h1>
          <p className="page-subtitle">{filtered.length} phase(s) au total</p>
        </div>
      </div>

      <div className="card mb-6">
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'center' }}>
          <div className="search-bar" style={{ flex: 1, minWidth: 200 }}>
            <Search size={15} color="var(--text-muted)" />
            <input placeholder="Libellé, code, projet..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Filter size={14} color="var(--text-muted)" />
            <select className="form-control" style={{ width: 'auto', padding: '8px 12px' }}
              value={filterProjet} onChange={e => setFilterProjet(e.target.value)}>
              <option value="">Tous les projets</option>
              {projets.map(p => <option key={p.id} value={p.id}>{p.nom}</option>)}
            </select>

            <select className="form-control" style={{ width: 'auto', padding: '8px 12px' }}
              value={filterEtat} onChange={e => setFilterEtat(e.target.value)}>
              <option value="">Tous les états</option>
              <option value="terminee">Réalisées</option>
              <option value="non-terminee">Non réalisées</option>
              <option value="facturee">Facturées</option>
              <option value="payee">Payées</option>
            </select>
          </div>
        </div>
      </div>

      <div className="grid-4 mb-6">
        {[
          { label: 'Total',      value: phases.length,                                  color: 'var(--accent-blue)' },
          { label: 'Réalisées',  value: phases.filter(p => p.etatRealisation).length,   color: 'var(--accent-emerald)' },
          { label: 'Facturées',  value: phases.filter(p => p.etatFacturation).length,   color: 'var(--accent-blue-light)' },
          { label: 'Payées',     value: phases.filter(p => p.etatPaiement).length,      color: 'var(--accent-amber)' },
        ].map(s => (
          <div key={s.label} className="stat-card">
            <div style={{ borderLeft: `3px solid ${s.color}`, paddingLeft: 12 }}>
              <div className="stat-label">{s.label}</div>
              <div className="stat-value">{s.value}</div>
            </div>
          </div>
        ))}
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state"><GitBranch size={40} /><p>Aucune phase trouvée</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Libellé</th>
                  <th>Projet</th>
                  <th>Période</th>
                  <th>Montant</th>
                  <th>État</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id}>
                    <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                    <td style={{ fontWeight: 500 }}>{p.libelle}</td>
                    <td>
                      <Link to={`/projets/${p.projetId}`}
                        style={{ color: 'var(--accent-blue-light)', textDecoration: 'none', fontSize: '0.85rem' }}>
                        {p.projetNom}
                      </Link>
                    </td>
                    <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                      {p.dateDebut} → {p.dateFin}
                    </td>
                    <td style={{ fontWeight: 600 }}>{p.montant?.toLocaleString('fr-FR')} MAD</td>
                    <td>{getStatusBadge(p)}</td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        <Link to={`/phases/${p.id}`} className="btn btn-secondary btn-sm btn-icon">
                          <ChevronRight size={13} />
                        </Link>
                        {hasRole('CHEF_PROJET', 'ADMINISTRATEUR') && (
                          <>
                            <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(p); setModal('edit'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-sm btn-icon" onClick={() => setDeleteId(p.id)}>
                              <Trash2 size={13} />
                            </button>
                          </>
                        )}
                      </div>
                      <div style={{ display: 'flex', gap: 4, marginTop: 4 }}>
                        {hasRole('CHEF_PROJET', 'ADMINISTRATEUR') && !p.etatRealisation && (
                          <button className="btn btn-success btn-xs" onClick={() => toggleState(p, 'realisation')} title="Marquer réalisée" style={{ padding: '2px 4px' }}>
                            <CheckCircle size={10} />
                          </button>
                        )}
                        {hasRole('COMPTABLE', 'ADMINISTRATEUR') && p.etatRealisation && !p.etatFacturation && (
                          <button className="btn btn-primary btn-xs" onClick={() => toggleState(p, 'facturation')} title="Marquer facturée" style={{ padding: '2px 4px' }}>
                            <Receipt size={10} />
                          </button>
                        )}
                        {hasRole('COMPTABLE', 'ADMINISTRATEUR') && p.etatFacturation && !p.etatPaiement && (
                          <button className="btn btn-warning btn-xs" onClick={() => toggleState(p, 'paiement')} title="Marquer payée" style={{ padding: '2px 4px' }}>
                            <DollarSign size={10} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <Modal open={modal === 'edit'} onClose={() => setModal(null)} title="Modifier la phase" size="lg">
        <PhaseForm initial={selected} projetId={selected?.projetId} onSave={handleUpdate} onClose={() => setModal(null)} />
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete} message="Supprimer cette phase ?" />
    </div>
  );
}
