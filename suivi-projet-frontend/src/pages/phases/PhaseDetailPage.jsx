import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { phaseService, affectationService, livrableService, employeService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { useForm } from 'react-hook-form';
import { ArrowLeft, Plus, CheckCircle, Circle, Users, Package, Pencil, Trash2 } from 'lucide-react';

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

function LivrableForm({ onSave, onClose, initial }) {
  const { register, handleSubmit, formState: { errors } } = useForm({ defaultValues: initial || {} });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        <div className="form-group">
          <label className="form-label">Code</label>
          <input className="form-control" placeholder="LIV-2026-001"
            {...register('code', { required: 'Requis' })} />
          {errors.code && <span className="form-error">{errors.code.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Libellé</label>
          <input className="form-control" placeholder="Dossier d'architecture"
            {...register('libelle', { required: 'Requis' })} />
          {errors.libelle && <span className="form-error">{errors.libelle.message}</span>}
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">Chemin / URL</label>
          <input className="form-control" placeholder="/uploads/livrable.pdf"
            {...register('chemin')} />
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">Description</label>
          <textarea className="form-control" rows={2} {...register('description')} />
        </div>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary">Enregistrer</button>
      </div>
    </form>
  );
}

export default function PhaseDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { hasRole } = useAuth();

  const [phase, setPhase]           = useState(null);
  const [affectations, setAffect]   = useState([]);
  const [livrables, setLivrables]   = useState([]);
  const [employes, setEmployes]     = useState([]);
  const [loading, setLoading]       = useState(true);
  const [tab, setTab]               = useState('affectations');
  const [modal, setModal]           = useState(null);
  const [selected, setSelected]     = useState(null);
  const [deleteTarget, setDel]      = useState(null);

  const load = async () => {
    try {
      const [phRes, affRes, livRes, empRes] = await Promise.all([
        phaseService.getById(id),
        affectationService.getByPhase(id),
        livrableService.getByPhase(id),
        employeService.getAll(),
      ]);
      setPhase(phRes.data);
      setAffect(affRes.data);
      setLivrables(livRes.data);
      setEmployes(empRes.data);
    } catch { toast.error('Erreur'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, [id]);

  const handleSaveAffect = async (data) => {
    try {
      const empId = selected?.employe?.id || data.employeId;
      const payload = { dateDebut: data.dateDebut, dateFin: data.dateFin };
      if (modal === 'edit-affect') {
        await affectationService.update(id, empId, payload);
        toast.success('Affectation mise à jour');
      } else {
        await affectationService.create(id, data.employeId, payload);
        toast.success('Affectation créée');
      }
      setModal(null); load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleSaveLivrable = async (data) => {
    try {
      if (modal === 'edit-liv') {
        await livrableService.update(selected.id, { ...data, phaseId: Number(id) });
        toast.success('Livrable mis à jour');
      } else {
        await livrableService.create(id, data);
        toast.success('Livrable créé');
      }
      setModal(null); load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      if (deleteTarget.type === 'affect') {
        await affectationService.delete(id, deleteTarget.employeId);
        toast.success('Affectation supprimée');
      } else {
        await livrableService.delete(deleteTarget.id);
        toast.success('Livrable supprimé');
      }
      load();
    } catch { toast.error('Erreur'); }
  };

  const toggleState = async (type) => {
    try {
      if (type === 'realisation') await phaseService.setRealisation(id);
      if (type === 'facturation') await phaseService.setFacturation(id);
      if (type === 'paiement')    await phaseService.setPaiement(id);
      toast.success('État mis à jour');
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  if (loading) return <div className="loading-center"><div className="spinner" /></div>;
  if (!phase) return <div className="empty-state"><p>Phase introuvable</p></div>;

  return (
    <div>
      <div className="page-header">
        <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
          <button className="btn btn-secondary btn-icon" onClick={() => navigate(-1)}>
            <ArrowLeft size={16} />
          </button>
          <div>
            <h1 className="page-title">{phase.libelle}</h1>
            <p className="page-subtitle"><code style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{phase.code}</code></p>
          </div>
        </div>
      </div>

      {/* Phase info card */}
      <div className="card mb-6">
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 24, marginBottom: 16 }}>
          <div><div className="stat-label">Montant</div><div style={{ fontWeight: 700, fontSize: '1.1rem', color: 'var(--accent-blue-light)' }}>{phase.montant?.toLocaleString('fr-FR')} MAD</div></div>
          <div><div className="stat-label">Période</div><div style={{ fontSize: '0.9rem' }}>{phase.dateDebut} → {phase.dateFin}</div></div>
          {phase.description && <div style={{ flex: 1 }}><div className="stat-label">Description</div><div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>{phase.description}</div></div>}
        </div>

        <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
          <span className={`badge ${phase.etatRealisation ? 'badge-green' : 'badge-gray'}`}>
            {phase.etatRealisation ? <CheckCircle size={11} /> : <Circle size={11} />} Réalisée
          </span>
          <span className={`badge ${phase.etatFacturation ? 'badge-blue' : 'badge-gray'}`}>
            {phase.etatFacturation ? <CheckCircle size={11} /> : <Circle size={11} />} Facturée
          </span>
          <span className={`badge ${phase.etatPaiement ? 'badge-amber' : 'badge-gray'}`}>
            {phase.etatPaiement ? <CheckCircle size={11} /> : <Circle size={11} />} Payée
          </span>

          <div style={{ marginLeft: 'auto', display: 'flex', gap: 8 }}>
            {hasRole('CHEF_PROJET','ADMINISTRATEUR') && !phase.etatRealisation && (
              <button className="btn btn-success btn-sm" onClick={() => toggleState('realisation')}>
                <CheckCircle size={12} /> Marquer réalisée
              </button>
            )}
            {hasRole('COMPTABLE','ADMINISTRATEUR') && phase.etatRealisation && !phase.etatFacturation && (
              <button className="btn btn-primary btn-sm" onClick={() => toggleState('facturation')}>
                Marquer facturée
              </button>
            )}
            {hasRole('COMPTABLE','ADMINISTRATEUR') && phase.etatFacturation && !phase.etatPaiement && (
              <button className="btn btn-warning btn-sm" onClick={() => toggleState('paiement')}>
                Marquer payée
              </button>
            )}
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div style={{ display: 'flex', gap: 4, marginBottom: 20, background: 'var(--bg-secondary)', padding: 4, borderRadius: 10, width: 'fit-content' }}>
        {[{ key: 'affectations', label: 'Affectations', icon: Users },
          { key: 'livrables', label: 'Livrables', icon: Package }].map(t => (
          <button key={t.key} onClick={() => setTab(t.key)} style={{
            display: 'flex', alignItems: 'center', gap: 6,
            padding: '7px 16px', borderRadius: 8, border: 'none', cursor: 'pointer',
            fontSize: '0.85rem', fontWeight: tab === t.key ? 600 : 400,
            background: tab === t.key ? 'var(--bg-card)' : 'transparent',
            color: tab === t.key ? 'var(--text-primary)' : 'var(--text-muted)',
          }}>
            <t.icon size={14} />{t.label}
          </button>
        ))}
      </div>

      {/* Affectations */}
      {tab === 'affectations' && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 14 }}>
            {hasRole('CHEF_PROJET','ADMINISTRATEUR') && (
              <button className="btn btn-primary btn-sm" onClick={() => { setSelected(null); setModal('add-affect'); }}>
                <Plus size={14} /> Affecter un employé
              </button>
            )}
          </div>
          {affectations.length === 0 ? (
            <div className="empty-state card"><Users size={36} /><p>Aucune affectation</p></div>
          ) : (
            <div className="card" style={{ padding: 0 }}>
              <div className="table-wrapper">
                <table>
                  <thead><tr><th>Employé</th><th>Profil</th><th>Période</th><th>Actions</th></tr></thead>
                  <tbody>
                    {affectations.map(a => (
                      <tr key={`${a.employe?.id}-${phase.id}`}>
                        <td>
                          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                            <div style={{ width: 30, height: 30, borderRadius: '50%', background: 'var(--bg-hover)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.72rem', fontWeight: 700, color: 'var(--accent-blue-light)' }}>
                              {a.employe?.prenom?.[0]}{a.employe?.nom?.[0]}
                            </div>
                            <div>
                              <div style={{ fontWeight: 500 }}>{a.employe?.prenom} {a.employe?.nom}</div>
                              <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{a.employe?.email}</div>
                            </div>
                          </div>
                        </td>
                        <td><span className="badge badge-blue">{a.employe?.profil?.libelle}</span></td>
                        <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                          {a.dateDebut?.substring(0,10)} → {a.dateFin?.substring(0,10)}
                        </td>
                        <td>
                          <div style={{ display: 'flex', gap: 6 }}>
                            {hasRole('CHEF_PROJET', 'ADMINISTRATEUR') && (
                              <>
                                <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(a); setModal('edit-affect'); }}>
                                  <Pencil size={13} />
                                </button>
                                <button className="btn btn-danger btn-sm btn-icon" onClick={() => setDel({ type: 'affect', employeId: a.employe?.id })}>
                                  <Trash2 size={13} />
                                </button>
                              </>
                            )}
                          </div>
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

      {/* Livrables */}
      {tab === 'livrables' && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 14 }}>
            {hasRole('CHEF_PROJET','DIRECTEUR','ADMINISTRATEUR') && (
              <button className="btn btn-primary btn-sm" onClick={() => { setSelected(null); setModal('add-liv'); }}>
                <Plus size={14} /> Ajouter un livrable
              </button>
            )}
          </div>
          {livrables.length === 0 ? (
            <div className="empty-state card"><Package size={36} /><p>Aucun livrable</p></div>
          ) : (
            <div className="card" style={{ padding: 0 }}>
              <div className="table-wrapper">
                <table>
                  <thead><tr><th>Code</th><th>Libellé</th><th>Description</th><th>Actions</th></tr></thead>
                  <tbody>
                    {livrables.map(l => (
                      <tr key={l.id}>
                        <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{l.code}</code></td>
                        <td style={{ fontWeight: 500 }}>{l.libelle}</td>
                        <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{l.description}</td>
                        <td>
                          <div style={{ display: 'flex', gap: 6 }}>
                            <a href={`http://localhost:8080/api/livrables/${l.id}/download`} className="btn btn-secondary btn-sm btn-icon" title="Télécharger" target="_blank" rel="noreferrer">↓</a>
                            {hasRole('CHEF_PROJET', 'DIRECTEUR', 'ADMINISTRATEUR') && (
                              <>
                                <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(l); setModal('edit-liv'); }}>
                                  <Pencil size={13} />
                                </button>
                                <button className="btn btn-danger btn-sm btn-icon" onClick={() => setDel({ type: 'liv', id: l.id })}>
                                  <Trash2 size={13} />
                                </button>
                              </>
                            )}
                          </div>
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

      {/* Modals */}
      <Modal open={modal === 'add-affect' || modal === 'edit-affect'} onClose={() => setModal(null)}
        title={modal === 'edit-affect' ? 'Modifier l\'affectation' : 'Affecter un employé'}>
        <AffectationForm employes={employes} onSave={handleSaveAffect} onClose={() => setModal(null)} initial={selected} />
      </Modal>

      <Modal open={modal === 'add-liv' || modal === 'edit-liv'} onClose={() => setModal(null)}
        title={modal === 'edit-liv' ? 'Modifier le livrable' : 'Nouveau livrable'} size="lg">
        <LivrableForm onSave={handleSaveLivrable} onClose={() => setModal(null)} initial={selected} />
      </Modal>

      <ConfirmModal open={!!deleteTarget} onClose={() => setDel(null)} onConfirm={handleDelete}
        message="Supprimer cet élément ?" />
    </div>
  );
}
