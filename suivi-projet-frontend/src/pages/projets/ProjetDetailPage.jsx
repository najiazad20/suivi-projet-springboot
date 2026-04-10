import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { projetService, phaseService, documentService, employeService, organismeService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { useForm } from 'react-hook-form';
import {
  ArrowLeft, Plus, GitBranch, FileText, CheckCircle,
  Circle, DollarSign, Pencil, Trash2, ChevronRight,
  TrendingUp, Users, Package
} from 'lucide-react';

// Phase form inline
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
      <input type="hidden" {...register('projetId', { valueAsNumber: true })} />
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

// Document form
function DocumentForm({ projetId, initial, onSave, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial || { projetId }
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        <div className="form-group">
          <label className="form-label">Code</label>
          <input className="form-control" placeholder="DOC-2026-001"
            {...register('code', { required: 'Requis' })} />
          {errors.code && <span className="form-error">{errors.code.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Libellé</label>
          <input className="form-control" placeholder="Cahier des charges"
            {...register('libelle', { required: 'Requis' })} />
          {errors.libelle && <span className="form-error">{errors.libelle.message}</span>}
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">Chemin / URL</label>
          <input className="form-control" placeholder="/uploads/projets/1/doc.pdf"
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

export default function ProjetDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { hasRole } = useAuth();

  const [projet, setProjet]     = useState(null);
  const [phases, setPhases]     = useState([]);
  const [docs, setDocs]         = useState([]);
  const [loading, setLoading]   = useState(true);
  const [tab, setTab]           = useState('phases');
  const [modal, setModal]       = useState(null);
  const [selected, setSelected] = useState(null);
  const [deleteId, setDeleteId] = useState(null);
  const [deleteType, setDeleteType] = useState(null);

  const load = async () => {
    try {
      const [pRes, phRes, dRes] = await Promise.all([
        projetService.getById(id),
        phaseService.getByProjet(id),
        documentService.getByProjet(id),
      ]);
      setProjet(pRes.data);
      setPhases(phRes.data);
      setDocs(dRes.data);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, [id]);

  const handleSavePhase = async (data) => {
    try {
      const payload = { ...data, projetId: Number(id), montant: Number(data.montant) };
      if (modal === 'edit-phase') {
        await phaseService.update(selected.id, payload);
        toast.success('Phase mise à jour');
      } else {
        await phaseService.create(payload);
        toast.success('Phase créée');
      }
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleSaveDoc = async (data) => {
    try {
      const payload = { ...data, projetId: Number(id) };
      if (modal === 'edit-doc') {
        await documentService.update(selected.id, payload);
        toast.success('Document mis à jour');
      } else {
        await documentService.create(id, payload);
        toast.success('Document ajouté');
      }
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      if (deleteType === 'phase') { await phaseService.delete(deleteId); toast.success('Phase supprimée'); }
      if (deleteType === 'doc')   { await documentService.delete(deleteId); toast.success('Document supprimé'); }
      load();
    } catch { toast.error('Erreur de suppression'); }
  };

  const togglePhaseState = async (phase, type) => {
    try {
      if (type === 'realisation') await phaseService.setRealisation(phase.id);
      if (type === 'facturation') await phaseService.setFacturation(phase.id);
      if (type === 'paiement')    await phaseService.setPaiement(phase.id);
      toast.success('État mis à jour');
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleCloseProject = async () => {
    try {
      if (projet.description?.includes('[CLÔTURÉ]')) return;
      const newDesc = (projet.description || '') + ' [CLÔTURÉ]';
      await projetService.update(id, { ...projet, description: newDesc });
      toast.success('Projet clôturé');
      load();
    } catch { toast.error('Erreur lors de la clôture'); }
  };

  if (loading) return <div className="loading-center"><div className="spinner" /></div>;
  if (!projet) return <div className="empty-state"><p>Projet introuvable</p></div>;

  const totalPhases = phases.reduce((s, p) => s + p.montant, 0);
  const progress = projet.montant > 0 ? (totalPhases / projet.montant) * 100 : 0;

  return (
    <div>
      {/* Header */}
      <div className="page-header">
        <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
          <button className="btn btn-secondary btn-icon" onClick={() => navigate('/projets')}>
            <ArrowLeft size={16} />
          </button>
          <div>
            <p className="page-subtitle"><code style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{projet.code}</code> · {projet.organisme?.nom}</p>
          </div>
        </div>
        <div>
          {hasRole('DIRECTEUR', 'ADMINISTRATEUR') && !projet.description?.includes('[CLÔTURÉ]') && (
            <button className="btn btn-danger" onClick={() => setModal('confirm-close')}>
              Clôturer le projet
            </button>
          )}
        </div>
      </div>

      {/* Summary cards */}
      <div className="grid-4 mb-6">
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(59,130,246,0.15)' }}>
            <DollarSign size={20} color="var(--accent-blue)" />
          </div>
          <div>
            <div className="stat-label">Budget</div>
            <div className="stat-value" style={{ fontSize: '1.2rem' }}>{(projet.montant/1000).toFixed(0)}k MAD</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(16,185,129,0.15)' }}>
            <GitBranch size={20} color="var(--accent-emerald)" />
          </div>
          <div>
            <div className="stat-label">Phases</div>
            <div className="stat-value">{phases.length}</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(139,92,246,0.15)' }}>
            <TrendingUp size={20} color="var(--accent-violet)" />
          </div>
          <div>
            <div className="stat-label">Alloué</div>
            <div className="stat-value" style={{ fontSize: '1.1rem' }}>{progress.toFixed(0)}%</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(6,182,212,0.15)' }}>
            <FileText size={20} color="var(--accent-cyan)" />
          </div>
          <div>
            <div className="stat-label">Documents</div>
            <div className="stat-value">{docs.length}</div>
          </div>
        </div>
      </div>

      {/* Progress bar */}
      <div className="card mb-6">
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8, fontSize: '0.8rem' }}>
          <span style={{ color: 'var(--text-muted)' }}>Budget alloué aux phases</span>
          <span style={{ color: 'var(--text-secondary)' }}>{totalPhases.toLocaleString('fr-FR')} / {projet.montant.toLocaleString('fr-FR')} MAD</span>
        </div>
        <div className="progress-bar">
          <div className="progress-fill" style={{ width: `${Math.min(progress, 100)}%`, background: progress > 90 ? 'linear-gradient(90deg, var(--accent-amber), var(--accent-rose))' : undefined }} />
        </div>
        <div style={{ display: 'flex', gap: 20, marginTop: 12, fontSize: '0.8rem', color: 'var(--text-muted)' }}>
          <span>Chef : {projet.chefProjet?.prenom} {projet.chefProjet?.nom}</span>
          <span>Période : {projet.dateDebut} → {projet.dateFin}</span>
        </div>
      </div>

      {/* Tabs */}
      <div style={{ display: 'flex', gap: 4, marginBottom: 20, background: 'var(--bg-secondary)', padding: 4, borderRadius: 10, width: 'fit-content' }}>
        {[
          { key: 'phases', label: 'Phases', icon: GitBranch },
          { key: 'documents', label: 'Documents', icon: FileText },
        ].map(t => (
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

      {/* Phases tab */}
      {tab === 'phases' && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 14 }}>
            {hasRole('CHEF_PROJET','ADMINISTRATEUR') && (
              <button className="btn btn-primary btn-sm" onClick={() => { setSelected(null); setModal('add-phase'); }}>
                <Plus size={14} /> Ajouter une phase
              </button>
            )}
          </div>
          {phases.length === 0 ? (
            <div className="empty-state card"><GitBranch size={36} /><p>Aucune phase définie</p></div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              {phases.map(phase => (
                <div key={phase.id} className="card" style={{ padding: '16px 20px' }}>
                  <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16 }}>
                    <div style={{ flex: 1 }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 6 }}>
                        <code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{phase.code}</code>
                        <span style={{ fontWeight: 600, fontSize: '0.95rem' }}>{phase.libelle}</span>
                      </div>
                      {phase.description && <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)', marginBottom: 10 }}>{phase.description}</p>}
                      <div style={{ display: 'flex', gap: 16, fontSize: '0.78rem', color: 'var(--text-muted)', marginBottom: 10 }}>
                        <span>{phase.dateDebut} → {phase.dateFin}</span>
                        <span style={{ color: 'var(--accent-blue-light)', fontWeight: 600 }}>{phase.montant?.toLocaleString('fr-FR')} MAD</span>
                      </div>

                      {/* State badges */}
                      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                        <span className={`badge ${phase.etatRealisation ? 'badge-green' : 'badge-gray'}`}>
                          {phase.etatRealisation ? <CheckCircle size={11} /> : <Circle size={11} />}
                          Réalisée
                        </span>
                        <span className={`badge ${phase.etatFacturation ? 'badge-blue' : 'badge-gray'}`}>
                          {phase.etatFacturation ? <CheckCircle size={11} /> : <Circle size={11} />}
                          Facturée
                        </span>
                        <span className={`badge ${phase.etatPaiement ? 'badge-amber' : 'badge-gray'}`}>
                          {phase.etatPaiement ? <CheckCircle size={11} /> : <Circle size={11} />}
                          Payée
                        </span>
                      </div>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, alignItems: 'flex-end' }}>
                      <div style={{ display: 'flex', gap: 6 }}>
                        <Link to={`/phases/${phase.id}`} className="btn btn-secondary btn-sm btn-icon" title="Détail">
                          <ChevronRight size={13} />
                        </Link>
                        {hasRole('CHEF_PROJET','ADMINISTRATEUR') && (
                          <>
                            <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(phase); setModal('edit-phase'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-sm btn-icon" onClick={() => { setDeleteId(phase.id); setDeleteType('phase'); }}>
                              <Trash2 size={13} />
                            </button>
                          </>
                        )}
                      </div>

                      {/* Action buttons */}
                      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap', justifyContent: 'flex-end' }}>
                        {hasRole('CHEF_PROJET','ADMINISTRATEUR') && !phase.etatRealisation && (
                          <button className="btn btn-success btn-sm" onClick={() => togglePhaseState(phase, 'realisation')}>
                            <CheckCircle size={12} /> Marquer réalisée
                          </button>
                        )}
                        {hasRole('COMPTABLE','ADMINISTRATEUR') && phase.etatRealisation && !phase.etatFacturation && (
                          <button className="btn btn-primary btn-sm" onClick={() => togglePhaseState(phase, 'facturation')}>
                            Marquer facturée
                          </button>
                        )}
                        {hasRole('COMPTABLE','ADMINISTRATEUR') && phase.etatFacturation && !phase.etatPaiement && (
                          <button className="btn btn-warning btn-sm" onClick={() => togglePhaseState(phase, 'paiement')}>
                            Marquer payée
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Documents tab */}
      {tab === 'documents' && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 14 }}>
            {hasRole('CHEF_PROJET','DIRECTEUR','SECRETAIRE','ADMINISTRATEUR') && (
              <button className="btn btn-primary btn-sm" onClick={() => { setSelected(null); setModal('add-doc'); }}>
                <Plus size={14} /> Ajouter un document
              </button>
            )}
          </div>
          {docs.length === 0 ? (
            <div className="empty-state card"><FileText size={36} /><p>Aucun document</p></div>
          ) : (
            <div className="card" style={{ padding: 0 }}>
              <div className="table-wrapper">
                <table>
                  <thead><tr><th>Code</th><th>Libellé</th><th>Description</th><th>Chemin</th><th>Actions</th></tr></thead>
                  <tbody>
                    {docs.map(d => (
                      <tr key={d.id}>
                        <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{d.code}</code></td>
                        <td style={{ fontWeight: 500 }}>{d.libelle}</td>
                        <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)', maxWidth: 200 }}>{d.description}</td>
                        <td>
                          <a href={`http://localhost:8080/api/documents/${d.id}/download`} target="_blank" rel="noreferrer"
                            style={{ color: 'var(--accent-blue-light)', fontSize: '0.78rem', textDecoration: 'none' }}>
                            Télécharger
                          </a>
                        </td>
                        <td>
                          <div style={{ display: 'flex', gap: 6 }}>
                            {hasRole('CHEF_PROJET', 'DIRECTEUR', 'SECRETAIRE', 'ADMINISTRATEUR') && (
                              <>
                                <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(d); setModal('edit-doc'); }}>
                                  <Pencil size={13} />
                                </button>
                                <button className="btn btn-danger btn-sm btn-icon" onClick={() => { setDeleteId(d.id); setDeleteType('doc'); }}>
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
      <Modal open={modal === 'add-phase' || modal === 'edit-phase'} onClose={() => setModal(null)}
        title={modal === 'edit-phase' ? 'Modifier la phase' : 'Nouvelle phase'} size="lg">
        <PhaseForm initial={selected} projetId={Number(id)} onSave={handleSavePhase} onClose={() => setModal(null)} />
      </Modal>

      <Modal open={modal === 'add-doc' || modal === 'edit-doc'} onClose={() => setModal(null)}
        title={modal === 'edit-doc' ? 'Modifier le document' : 'Nouveau document'} size="lg">
        <DocumentForm projetId={Number(id)} initial={selected} onSave={handleSaveDoc} onClose={() => setModal(null)} />
      </Modal>


      <ConfirmModal open={modal === 'confirm-close'} onClose={() => setModal(null)} onConfirm={handleCloseProject}
        message="Voulez-vous vraiment clôturer ce projet ? Cette action est irréversible dans l'affichage." />

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete}
        message={`Supprimer ${deleteType === 'phase' ? 'cette phase' : 'ce document'} ?`} />
    </div>
  );
}
