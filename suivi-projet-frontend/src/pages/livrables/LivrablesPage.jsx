import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projetService, phaseService, livrableService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Search, Package, Download, Pencil, Trash2 } from 'lucide-react';

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

export default function LivrablesPage() {
  const { hasRole } = useAuth();
  const [items, setItems]       = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [search, setSearch]     = useState('');
  const [modal, setModal]       = useState(null);
  const [selected, setSelected] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const projRes = await projetService.getAll();
      const allPhases = await Promise.all(
        projRes.data.map(p =>
          phaseService.getByProjet(p.id)
            .then(r => r.data.map(ph => ({ ...ph, projetNom: p.nom, projetId: p.id })))
            .catch(() => [])
        )
      );
      const phases = allPhases.flat();

      const allLivrables = await Promise.all(
        phases.map(ph =>
          livrableService.getByPhase(ph.id)
            .then(r => r.data.map(l => ({ ...l, phaseLibelle: ph.libelle, phaseId: ph.id, projetNom: ph.projetNom, projetId: ph.projetId })))
            .catch(() => [])
        )
      );
      const flat = allLivrables.flat();
      setItems(flat);
      setFiltered(flat);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    if (!search) { setFiltered(items); return; }
    const q = search.toLowerCase();
    setFiltered(items.filter(l =>
      l.libelle?.toLowerCase().includes(q) ||
      l.code?.toLowerCase().includes(q) ||
      l.phaseLibelle?.toLowerCase().includes(q) ||
      l.projetNom?.toLowerCase().includes(q)
    ));
  }, [search, items]);

  const handleUpdate = async (data) => {
    try {
      await livrableService.update(selected.id, data);
      toast.success('Livrable mis à jour');
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      await livrableService.delete(deleteId);
      toast.success('Livrable supprimé');
      setDeleteId(null);
      load();
    } catch { toast.error('Erreur'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Livrables</h1>
          <p className="page-subtitle">{filtered.length} livrable(s)</p>
        </div>
      </div>

      <div className="card mb-6">
        <div className="search-bar" style={{ maxWidth: 400 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Code, libellé, phase, projet..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state"><Package size={40} /><p>Aucun livrable</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Libellé</th>
                  <th>Phase</th>
                  <th>Projet</th>
                   <th>Description</th>
                  <th>Fichier</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(l => (
                  <tr key={l.id}>
                    <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{l.code}</code></td>
                    <td style={{ fontWeight: 500 }}>{l.libelle}</td>
                    <td>
                      <Link to={`/phases/${l.phaseId}`}
                        style={{ color: 'var(--accent-blue-light)', textDecoration: 'none', fontSize: '0.85rem' }}>
                        {l.phaseLibelle}
                      </Link>
                    </td>
                    <td>
                      <Link to={`/projets/${l.projetId}`}
                        style={{ color: 'var(--text-secondary)', textDecoration: 'none', fontSize: '0.82rem' }}>
                        {l.projetNom}
                      </Link>
                    </td>
                    <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)', maxWidth: 200 }}>{l.description}</td>
                    <td>
                      {l.chemin && (
                        <a
                          href={`http://localhost:8080/api/livrables/${l.id}/download`}
                          target="_blank" rel="noreferrer"
                          className="btn btn-secondary btn-sm btn-icon"
                          title="Télécharger"
                        >
                          <Download size={13} />
                        </a>
                      )}
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        {hasRole('CHEF_PROJET', 'DIRECTEUR', 'ADMINISTRATEUR') && (
                          <>
                            <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(l); setModal('edit'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-sm btn-icon" onClick={() => setDeleteId(l.id)}>
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
        )}
      </div>

      <Modal open={modal === 'edit'} onClose={() => setModal(null)} title="Modifier le livrable" size="lg">
        <LivrableForm initial={selected} onSave={handleUpdate} onClose={() => setModal(null)} />
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete} message="Supprimer ce livrable ?" />
    </div>
  );
}
