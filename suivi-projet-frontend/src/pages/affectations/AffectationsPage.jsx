import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { employeService, affectationService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Search, UserCheck, ChevronRight, Pencil, Trash2 } from 'lucide-react';

function AffectationForm({ employes, onSave, onClose, initial }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial ? {
      ...initial,
      dateDebut: initial.dateDebut?.substring(0, 10),
      dateFin: initial.dateFin?.substring(0, 10),
    } : {}
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      {!initial && (
        <div className="form-group">
          <label className="form-label">Employé</label>
          <select className="form-control" {...register('employeId', { required: 'Requis', valueAsNumber: true })}>
            <option value="">-- Sélectionner --</option>
            {employes.map(e => <option key={e.id} value={e.id}>{e.prenom} {e.nom} · {e.profil?.libelle}</option>)}
          </select>
          {errors.employeId && <span className="form-error">{errors.employeId.message}</span>}
        </div>
      )}
      <div className="form-grid">
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
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary">Enregistrer</button>
      </div>
    </form>
  );
}

export default function AffectationsPage() {
  const { hasRole } = useAuth();
  const [employes, setEmployes]   = useState([]);
  const [data, setData]           = useState([]); // [{employe, phases:[]}]
  const [filtered, setFiltered]   = useState([]);
  const [loading, setLoading]     = useState(true);
  const [search, setSearch]       = useState('');
  const [modal, setModal]         = useState(null);
  const [selected, setSelected]   = useState(null);
  const [deleteTarget, setDel]    = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const empRes = await employeService.getAll();
      setEmployes(empRes.data);
      // For each employee, get their phases
      const rows = await Promise.all(
        empRes.data.map(async e => {
          try {
            const r = await affectationService.getByEmploye(e.id);
            return { employe: e, phases: r.data };
          } catch { return { employe: e, phases: [] }; }
        })
      );
      const active = rows.filter(r => r.phases.length > 0);
      setData(active);
      setFiltered(active);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    if (!search) { setFiltered(data); return; }
    const q = search.toLowerCase();
    setFiltered(data.filter(d =>
      d.employe.nom?.toLowerCase().includes(q) ||
      d.employe.prenom?.toLowerCase().includes(q) ||
      d.employe.matricule?.toLowerCase().includes(q)
    ));
  }, [search, data]);

  const handleUpdate = async (formData) => {
    try {
      const { phaseId, employeId } = selected;
      await affectationService.update(phaseId, employeId, {
        dateDebut: formData.dateDebut,
        dateFin: formData.dateFin
      });
      toast.success('Affectation mise à jour');
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      await affectationService.delete(deleteTarget.phaseId, deleteTarget.employeId);
      toast.success('Affectation supprimée');
      setDel(null);
      load();
    } catch { toast.error('Erreur'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Affectations</h1>
          <p className="page-subtitle">Vue globale des affectations employé–phase</p>
        </div>
      </div>

      <div className="card mb-6">
        <div className="search-bar" style={{ maxWidth: 400 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Nom, prénom, matricule..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      {loading ? (
        <div className="loading-center"><div className="spinner" /></div>
      ) : filtered.length === 0 ? (
        <div className="empty-state card"><UserCheck size={40} /><p>Aucune affectation</p></div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          {filtered.map(({ employe, phases }) => (
            <div key={employe.id} className="card">
              <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 14 }}>
                <div style={{
                  width: 38, height: 38, borderRadius: '50%',
                  background: 'linear-gradient(135deg, var(--accent-blue), var(--accent-violet))',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontSize: '0.85rem', fontWeight: 700, color: '#fff', flexShrink: 0,
                }}>
                  {employe.prenom?.[0]}{employe.nom?.[0]}
                </div>
                <div>
                  <div style={{ fontWeight: 600, fontSize: '0.95rem' }}>{employe.prenom} {employe.nom}</div>
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                    {employe.matricule} · {employe.profil?.libelle}
                  </div>
                </div>
                <span className="badge badge-blue" style={{ marginLeft: 'auto' }}>
                  {phases.length} phase(s)
                </span>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {phases.map(a => (
                  <div key={a.phase?.id} style={{
                    display: 'flex', alignItems: 'center', justifyContent: 'space-between',
                    background: 'var(--bg-secondary)', borderRadius: 8, padding: '10px 14px',
                    border: '1px solid var(--border)',
                  }}>
                    <div>
                      <div style={{ fontWeight: 500, fontSize: '0.875rem' }}>{a.phase?.libelle}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                        {a.dateDebut?.substring(0,10)} → {a.dateFin?.substring(0,10)}
                      </div>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                      <span className={`badge ${a.phase?.etatRealisation ? 'badge-green' : 'badge-gray'}`} style={{ fontSize: '0.7rem' }}>
                        {a.phase?.etatRealisation ? 'Réalisée' : 'En cours'}
                      </span>
                      <Link to={`/phases/${a.phase?.id}`} className="btn btn-secondary btn-sm btn-icon">
                        <ChevronRight size={13} />
                      </Link>
                      {hasRole('CHEF_PROJET', 'ADMINISTRATEUR') && (
                        <>
                          <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected({ ...a, phaseId: a.phase?.id, employeId: employe.id }); setModal('edit'); }}>
                            <Pencil size={13} />
                          </button>
                          <button className="btn btn-danger btn-sm btn-icon" onClick={() => setDel({ phaseId: a.phase?.id, employeId: employe.id })}>
                            <Trash2 size={13} />
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal open={modal === 'edit'} onClose={() => setModal(null)} title="Modifier l'affectation">
        <AffectationForm employes={employes} initial={selected} onSave={handleUpdate} onClose={() => setModal(null)} />
      </Modal>

      <ConfirmModal open={!!deleteTarget} onClose={() => setDel(null)} onConfirm={handleDelete} message="Supprimer cette affectation ?" />
    </div>
  );
}
