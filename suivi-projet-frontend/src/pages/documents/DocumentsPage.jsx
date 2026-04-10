import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projetService, documentService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Search, FileText, Download, Pencil, Trash2, Plus } from 'lucide-react';

function DocumentForm({ projets, initial, onSave, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial ? {
      ...initial,
      projetId: initial.projetId
    } : {}
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        {!initial && (
          <div className="form-group" style={{ gridColumn: 'span 2' }}>
            <label className="form-label">Projet</label>
            <select className="form-control" {...register('projetId', { required: 'Requis', valueAsNumber: true })}>
              <option value="">-- Sélectionner un projet --</option>
              {projets?.map(p => <option key={p.id} value={p.id}>{p.nom}</option>)}
            </select>
            {errors.projetId && <span className="form-error">{errors.projetId.message}</span>}
          </div>
        )}
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

export default function DocumentsPage() {
  const { hasRole } = useAuth();
  const [projets, setProjets]   = useState([]);
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
      setProjets(projRes.data);
      const allDocs = await Promise.all(
        projRes.data.map(p =>
          documentService.getByProjet(p.id)
            .then(r => r.data.map(d => ({ ...d, projetNom: p.nom, projetId: p.id })))
            .catch(() => [])
        )
      );
      const flat = allDocs.flat();
      setItems(flat);
      setFiltered(flat);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    if (!search) { setFiltered(items); return; }
    const q = search.toLowerCase();
    setFiltered(items.filter(d =>
      d.libelle?.toLowerCase().includes(q) ||
      d.code?.toLowerCase().includes(q) ||
      d.projetNom?.toLowerCase().includes(q)
    ));
  }, [search, items]);

  const handleCreate = async (data) => {
    try {
      await documentService.create(data.projetId, data);
      toast.success('Document ajouté');
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleUpdate = async (data) => {
    try {
      await documentService.update(selected.id, data);
      toast.success('Document mis à jour');
      setModal(null);
      load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      await documentService.delete(deleteId);
      toast.success('Document supprimé');
      setDeleteId(null);
      load();
    } catch { toast.error('Erreur'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Documents</h1>
          <p className="page-subtitle">{filtered.length} document(s)</p>
        </div>
        {hasRole('SECRETAIRE', 'CHEF_PROJET', 'ADMINISTRATEUR') && (
          <button className="btn btn-primary" onClick={() => { setSelected(null); setModal('add'); }}>
            <Plus size={16} />
            <span>Nouveau Document</span>
          </button>
        )}
      </div>

      <div className="card mb-6">
        <div className="search-bar" style={{ maxWidth: 400 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Code, libellé, projet..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state"><FileText size={40} /><p>Aucun document</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Libellé</th>
                  <th>Projet</th>
                   <th>Description</th>
                  <th>Fichier</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(d => (
                  <tr key={d.id}>
                    <td><code style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{d.code}</code></td>
                    <td style={{ fontWeight: 500 }}>{d.libelle}</td>
                    <td>
                      <Link to={`/projets/${d.projetId}`}
                        style={{ color: 'var(--accent-blue-light)', textDecoration: 'none', fontSize: '0.85rem' }}>
                        {d.projetNom}
                      </Link>
                    </td>
                    <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)', maxWidth: 220 }}>{d.description}</td>
                    <td>
                      {d.chemin && (
                        <a
                          href={`http://localhost:8080/api/documents/${d.id}/download`}
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
                        {hasRole('CHEF_PROJET', 'DIRECTEUR', 'SECRETAIRE', 'ADMINISTRATEUR') && (
                          <>
                            <button className="btn btn-secondary btn-sm btn-icon" onClick={() => { setSelected(d); setModal('edit'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-sm btn-icon" onClick={() => setDeleteId(d.id)}>
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

      <Modal open={modal === 'add'} onClose={() => setModal(null)} title="Ajouter un document" size="lg">
        <DocumentForm projets={projets} onSave={handleCreate} onClose={() => setModal(null)} />
      </Modal>

      <Modal open={modal === 'edit'} onClose={() => setModal(null)} title="Modifier le document" size="lg">
        <DocumentForm projets={projets} initial={selected} onSave={handleUpdate} onClose={() => setModal(null)} />
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete} message="Supprimer ce document ?" />
    </div>
  );
}
